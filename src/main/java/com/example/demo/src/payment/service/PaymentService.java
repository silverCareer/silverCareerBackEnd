package com.example.demo.src.payment.service;


import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.bid.domain.Bid;
import com.example.demo.src.bid.repository.BidRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.payment.domain.Payment;
import com.example.demo.src.payment.dto.RequestPayment;
import com.example.demo.src.payment.dto.ResponsePayment;
import com.example.demo.src.payment.dto.ResponsePaymentHistory;
import com.example.demo.src.payment.repository.PaymentRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.suggestion.domain.Suggestion;
import com.example.demo.src.suggestion.repository.SuggestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    @Transactional
    public ResponsePayment doProductPayment(RequestPayment requestPayment, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long memberBalance = member.getBalance();
        String paymentName = requestPayment.getPaymentName();
        requestPayment  = requestPayment.withCurrentTime(paymentName);
        Product product = productRepository.findProductByProductName(paymentName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long productPrice = product.getPrice();

        if(validateMemberBalance(memberBalance, productPrice)){
            // payment 테이블에 기록
            updateExchangeRecordProduct(requestPayment, paymentRepository, member, productPrice);
            // member balance 차감
            deductMemberBalance(memberRepository, memberEmail, productPrice);
        }
        return ResponsePayment.builder()
                .paymentName(paymentName)
                .price(productPrice)
                .build();
    }

    @Transactional
    public ResponsePayment doBidPayment(RequestPayment requestPayment, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long memberBalance = member.getBalance();
        String paymentName = requestPayment.getPaymentName();
        requestPayment  = requestPayment.withCurrentTime(paymentName);
        Suggestion suggestion = suggestionRepository.findByTitle(paymentName)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Bid bid = bidRepository.findBySuggestion_SuggestionIdx(suggestion.getSuggestionIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

        Long bidPrice = bid.getPrice();

        if(validateMemberBalance(memberBalance, bidPrice)){
            // payment 테이블에 기록
            updateExchangeRecordBid(requestPayment, paymentRepository, member, bidPrice);
            // member balance 차감
            deductMemberBalance(memberRepository, memberEmail, bidPrice);
        }
        return ResponsePayment.builder()
                .paymentName(paymentName)
                .price(bidPrice)
                .build();
    }

    @Transactional
    public List<ResponsePaymentHistory> getPaymentHistory(String memberEmail) {
        List<Payment> paymentList = paymentRepository.findPaymentsByMember_Username(memberEmail).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_PAYMENT_HISTORY));

        return paymentList.stream().map(ResponsePaymentHistory::of).collect(Collectors.toList());
    }

    private static void updateExchangeRecordProduct(RequestPayment requestPayment, PaymentRepository paymentRepository, Member member, Long productPrice){
        Payment payment = Payment.builder()
                .paymentDate(requestPayment.getLocalDate())
                .paymentAmount(productPrice)
                .paymentSuccess(Boolean.parseBoolean("true"))
                .paymentType("상품")
                .paymentName(requestPayment.getPaymentName())
                .member(member)
                .build();
        paymentRepository.save(payment);
    }

    private static void updateExchangeRecordBid(RequestPayment requestPayment, PaymentRepository paymentRepository, Member member, Long productPrice){
        Payment payment = Payment.builder()
                .paymentDate(requestPayment.getLocalDate())
                .paymentAmount(productPrice)
                .paymentSuccess(Boolean.parseBoolean("true"))
                .paymentType("입찰")
                .paymentName(requestPayment.getPaymentName())
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
