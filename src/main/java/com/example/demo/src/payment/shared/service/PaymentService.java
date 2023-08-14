package com.example.demo.src.payment.shared.service;


import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.payment.shared.domain.Payment;
import com.example.demo.src.payment.shared.dto.RequestPayment;
import com.example.demo.src.payment.shared.dto.ResponsePayment;
import com.example.demo.src.payment.shared.dto.ResponsePaymentHistory;
import com.example.demo.src.payment.shared.repository.PaymentRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    @Transactional
    public ResponsePayment doPayment(RequestPayment requestPayment, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long memberBalance = member.getBalance();

        Long productId = requestPayment.getProductId();
        requestPayment  = requestPayment.withCurrentTime(productId);
        Product product = productRepository.findProductByProductIdx(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Long productPrice = product.getPrice();

        if(validateMemberBalance(memberBalance, productPrice)){
            // payment 테이블에 기록
            updateExchangeRecord(requestPayment, paymentRepository, member, product, productPrice);
            // member balance 차감
            deductMemberBalance(memberRepository, memberEmail, productPrice);
        }
        return ResponsePayment.builder()
                .productIdx(productId)
                .price(productPrice)
                .build();
    }

    @Transactional
    public List<ResponsePaymentHistory> getPaymentHistory(String memberEmail) {
        List<Payment> paymentList = paymentRepository.findPaymentsByMember_Username(memberEmail).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_PAYMENT_HISTORY));;

        return paymentList.stream().map(ResponsePaymentHistory::of).collect(Collectors.toList());
    }

    private static void updateExchangeRecord(RequestPayment requestPayment, PaymentRepository paymentRepository, Member member, Product product, Long productPrice){
        Payment payment = Payment.builder()
                .paymentDate(requestPayment.getLocalDate())
                .paymentAmount(productPrice)
                .paymentSuccess(Boolean.parseBoolean("true"))
                .product(product)
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
            throw new IllegalArgumentException("잔액이 충분하지 않습니다.");
        }
        return true;
    }
}
