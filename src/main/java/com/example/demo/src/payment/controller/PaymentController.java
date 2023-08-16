package com.example.demo.src.payment.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.payment.service.PaymentService;
import com.example.demo.src.payment.dto.RequestPayment;
import com.example.demo.src.payment.dto.ResponsePayment;
import com.example.demo.src.payment.dto.ResponsePaymentHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    // 상품 결제
    @PostMapping("/productPayment")
    public ResponseEntity<ResponsePayment> productPayment(@RequestBody RequestPayment requestPayment, @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {
        ResponsePayment responsePayment = paymentService.doProductPayment(requestPayment, memberEmail);

        return new ResponseEntity<>(responsePayment, HttpStatus.OK);
    }

    // 입찰 결제
    @PostMapping("/bidPayment")
    public ResponseEntity<ResponsePayment> bidPayment(@RequestBody RequestPayment requestPayment, @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {
        ResponsePayment responsePayment = paymentService.doBidPayment(requestPayment, memberEmail);

        return new ResponseEntity<>(responsePayment, HttpStatus.OK);
    }

    // 결제 내역 조회
    @GetMapping("/paymentHistory")
    public ResponseEntity<CommonResponse> getPaymentHistory(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        List<ResponsePaymentHistory> responsePaymentHistoryList = paymentService.getPaymentHistory(memberEmail);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(responsePaymentHistoryList)
                .build());
    }
}
