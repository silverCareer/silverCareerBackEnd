package com.example.demo.src.payment.service;


import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.bid.domain.Bid;
import com.example.demo.src.bid.repository.BidRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.payment.domain.Payment;
import com.example.demo.src.payment.dto.*;
import com.example.demo.src.payment.repository.PaymentRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.review.domain.Review;
import com.example.demo.src.review.repository.ReviewRepository;
import com.example.demo.src.suggestion.domain.Suggestion;
import com.example.demo.src.suggestion.repository.SuggestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final SuggestionRepository suggestionRepository;
    private final BidRepository bidRepository;
    private final ReviewRepository reviewRepository;
    @Transactional
    public ResponsePayment doProductPayment(RequestPayment requestPayment, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long memberBalance = member.getBalance();
        Long productIdx = requestPayment.getProductIdx();

        Product product = productRepository.findProductByProductIdx(productIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long productPrice = product.getPrice();
        String productName = product.getProductName();

        if(validateMemberBalance(memberBalance, productPrice)){
            // payment 테이블에 기록
            updateExchangeRecordProduct(paymentRepository, member, productPrice, productName, productIdx);
            // member balance 차감
            deductMemberBalance(memberRepository, memberEmail, productPrice);
        }
        return ResponsePayment.builder()
                .productIdx(productIdx)
                .price(productPrice)
                .build();
    }

    @Transactional
    public ResponseBidPayment doBidPayment(RequestBidPayment requestBidPayment, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long memberBalance = member.getBalance();
        Long bidIdx = requestBidPayment.getBidIdx();
        Bid bid = bidRepository.findByBidIdx(bidIdx);
        Long bidPrice = bid.getPrice();
        Long suggestionIdx = bid.getSuggestion().getSuggestionIdx();

        Suggestion suggestion = suggestionRepository.findById(suggestionIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        String bidName = suggestion.getTitle();

        if(validateMemberBalance(memberBalance, bidPrice)){
            // payment 테이블에 기록
            updateExchangeRecordBid(paymentRepository, member, bidName, bidPrice, bidIdx);
            // member balance 차감
            deductMemberBalance(memberRepository, memberEmail, bidPrice);
        }
        return ResponseBidPayment.builder()
                .bidIdx(bidIdx)
                .price(bidPrice)
                .build();
    }

    @Transactional
    public List<ResponsePaymentHistory> getPaymentHistory(String memberEmail) {
        List<Payment> paymentList = paymentRepository.findPaymentsByMember_Username(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PAYMENT_HISTORY));

        List<ResponsePaymentHistory> responsePaymentHistoryList = new ArrayList<>();
        String mentorName = "";
        for (Payment payment : paymentList) {
            Long paymentIdx = payment.getProductIdx(); // product title 조회
            String paymentType = payment.getPaymentType();
            if(paymentType.equals("상품")){
                Product product = productRepository.findProductsByProductIdx(paymentIdx)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
                String email = product.getMember().getUsername();
                Member member = memberRepository.findByUsername(email)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
                mentorName = member.getName();
                Boolean reviewed = false;
                Long cnt = reviewRepository.checkReviewer(paymentIdx, memberEmail);
                if(cnt == 1){
                    reviewed = true;
                }
                responsePaymentHistoryList.add(ResponsePaymentHistory.of(payment, reviewed, mentorName));
            } else {
                Bid bid = bidRepository.findBidsByBidIdx(paymentIdx)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
                String email = bid.getMember().getUsername();
                Member member = memberRepository.findByUsername(email)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
                mentorName = member.getName();
                responsePaymentHistoryList.add(ResponsePaymentHistory.of(payment, true, mentorName));
            }
        }

        return responsePaymentHistoryList;
    }

//    @Transactional
//    public List<ResponsePaymentHistory> getPaymentHistory(String memberEmail) {
//        List<Payment> paymentList = paymentRepository.findPaymentsByMember_Username(memberEmail).orElseThrow(()
//                -> new CustomException(ErrorCode.NOT_FOUND_PAYMENT_HISTORY));
//
//        return paymentList.stream().map(ResponsePaymentHistory::of).collect(Collectors.toList());
//    }

    private static void updateExchangeRecordProduct(PaymentRepository paymentRepository, Member member, Long productPrice, String productName, Long productIdx){
        Payment payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .paymentAmount(productPrice)
                .paymentSuccess(Boolean.parseBoolean("true"))
                .paymentType("상품")
                .paymentName(productName)
                .productIdx(productIdx)
                .member(member)
                .build();
        paymentRepository.save(payment);
    }

    private static void updateExchangeRecordBid(PaymentRepository paymentRepository, Member member, String bidName, Long bidPrice, Long bidIdx){
        Payment payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .paymentAmount(bidPrice)
                .paymentSuccess(Boolean.parseBoolean("true"))
                .paymentType("입찰")
                .paymentName(bidName)
                .productIdx(bidIdx)
                .member(member)
                .build();
        paymentRepository.save(payment);
    }

    private static void deductMemberBalance(MemberRepository memberRepository, String memberEmail, Long productPrice) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        member.deductCash(productPrice);

        memberRepository.save(member);
    }

    public boolean validateMemberBalance(Long memberBalance, Long productPrice){
        if(memberBalance < productPrice){
            throw new CustomException(ErrorCode.NOT_ENOUGH_MEMBER_BALANCE);
        }
        return true;
    }
}
