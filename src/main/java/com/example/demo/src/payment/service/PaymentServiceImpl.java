package com.example.demo.src.payment.service;


import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.bid.NotFoundBidException;
import com.example.demo.global.exception.error.payment.NotFoundPaymentHistoryException;
import com.example.demo.global.exception.error.payment.WrongPaymentInputException;
import com.example.demo.global.exception.error.member.NotFoundMemberException;
import com.example.demo.global.exception.error.product.NotFoundProductListException;
import com.example.demo.global.exception.error.suggestion.NotFoundSuggestionException;
import com.example.demo.src.bid.domain.Bid;
import com.example.demo.src.bid.domain.BidStatus;
import com.example.demo.src.bid.repository.BidRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.payment.domain.Payment;
import com.example.demo.src.payment.dto.*;
import com.example.demo.src.payment.repository.PaymentRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.review.repository.ReviewRepository;
import com.example.demo.src.suggestion.domain.Suggestion;
import com.example.demo.src.suggestion.repository.SuggestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final SuggestionRepository suggestionRepository;
    private final BidRepository bidRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> doProductPayment(RequestPayment requestPayment, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(NotFoundMemberException::new);
        Long memberBalance = member.getBalance();
        Long productIdx = requestPayment.getProductIdx();

        Product product = productRepository.findProductByProductIdx(productIdx)
                .orElseThrow(NotFoundProductListException::new);
        Long productPrice = product.getPrice();
        String productName = product.getProductName();

        if(validateMemberBalance(memberBalance, productPrice)){
            // payment 테이블에 기록
            updateExchangeRecordProduct(paymentRepository, member, productPrice, productName, productIdx);
            // member balance 차감
            member.deductCash(productPrice);
        }
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("상품 결제 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> doBidPayment(RequestBidPayment requestBidPayment, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(NotFoundMemberException::new);
        Long memberBalance = member.getBalance();
        Long bidIdx = requestBidPayment.getBidIdx();
        Bid bid = bidRepository.findById(bidIdx)
                .orElseThrow(NotFoundBidException::new);
        Long bidPrice = bid.getPrice();
        Long suggestionIdx = bid.getSuggestion().getSuggestionIdx();

        Suggestion suggestion = suggestionRepository.findById(suggestionIdx)
                .orElseThrow(NotFoundSuggestionException::new);
        String bidName = suggestion.getTitle();

        if(validateMemberBalance(memberBalance, bidPrice)){
            // payment 테이블에 기록
            updateExchangeRecordBid(paymentRepository, member, bidName, bidPrice, bidIdx);
            // member balance 차감
            member.deductCash(bidPrice);
            // 결제한 입찰 건 종료 및 같은 의뢰에 대한 입찰 건 삭제
            terminateBid(suggestion, bid);
        }
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("입찰 결제 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> getPaymentHistory(String memberEmail) {
        List<Payment> paymentList = paymentRepository.findPaymentsByMember_Username(memberEmail)
                .orElseThrow(NotFoundPaymentHistoryException::new);
        if(paymentList.isEmpty()) throw new NotFoundPaymentHistoryException();

        List<ResponsePaymentHistory> responsePaymentHistoryList = new ArrayList<>();
        String mentorName = "";
        for (Payment payment : paymentList) {
            Long paymentIdx = payment.getProductIdx(); // product title 조회
            String paymentType = payment.getPaymentType();
            if(paymentType.equals("상품")){
                Product product = productRepository.findProductsByProductIdx(paymentIdx)
                        .orElseThrow(NotFoundProductListException::new);

                String email = product.getMember().getUsername();
                Member member = memberRepository.findByUsername(email)
                        .orElseThrow(NotFoundMemberException::new);
                mentorName = member.getName();
                boolean reviewed = false;
                long cnt = reviewRepository.checkReviewer(paymentIdx, memberEmail);
                if(cnt == 1){
                    reviewed = true;
                }
                responsePaymentHistoryList.add(ResponsePaymentHistory.of(payment, reviewed, mentorName));
            } else {
                Bid bid = bidRepository.findBidsByBidIdx(paymentIdx)
                        .orElseThrow(NotFoundBidException::new);
                String email = bid.getMember().getUsername();
                Member member = memberRepository.findByUsername(email)
                        .orElseThrow(NotFoundMemberException::new);
                mentorName = member.getName();
                responsePaymentHistoryList.add(ResponsePaymentHistory.of(payment, true, mentorName));
            }
        }

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(responsePaymentHistoryList).build());
    }

    private void updateExchangeRecordProduct(PaymentRepository paymentRepository, Member member, Long productPrice, String productName, Long productIdx){
        Payment payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .paymentAmount(productPrice)
                .paymentSuccess(true)
                .paymentType("상품")
                .paymentName(productName)
                .productIdx(productIdx)
                .member(member)
                .build();
        paymentRepository.save(payment);
    }

    private void updateExchangeRecordBid(PaymentRepository paymentRepository, Member member, String bidName, Long bidPrice, Long bidIdx){
        Payment payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .paymentAmount(bidPrice)
                .paymentSuccess(true)
                .paymentType("입찰")
                .paymentName(bidName)
                .productIdx(bidIdx)
                .member(member)
                .build();
        paymentRepository.save(payment);
    }

    private boolean validateMemberBalance(Long memberBalance, Long productPrice){
        if(memberBalance < productPrice){
            throw new WrongPaymentInputException();
        }
        return true;
    }

    private void terminateBid(Suggestion suggestion, Bid bid){
        bid.updateStatus(BidStatus.완료);
        suggestion.terminateSuggestion(true);

        List<Bid> notAcceptedBids = bidRepository.findBidsBySuggestion_SuggestionIdx(suggestion.getSuggestionIdx());
        List<Long> bidIdxToDelete = notAcceptedBids.stream()
                .filter(Bid -> !Bid.getBidIdx().equals(bid.getBidIdx()))
                .map(Bid::getBidIdx)
                .collect(Collectors.toList());

        if(!bidIdxToDelete.isEmpty()){
            bidRepository.deleteBidsByIdIn(bidIdxToDelete);
        }
    }


}
