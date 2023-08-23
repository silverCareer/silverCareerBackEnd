package com.example.demo.src.payment.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.payment.dto.*;
import com.example.demo.src.payment.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    // 상품 결제
    @PostMapping("/productPayment")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> productPayment(@RequestBody RequestPayment requestPayment, @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {
//        ResponsePayment responsePayment = paymentService.doProductPayment(requestPayment, memberEmail);

        return paymentService.doProductPayment(requestPayment, memberEmail);
    }

//     입찰 결제
    @PostMapping("/bidPayment")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> bidPayment(@RequestBody RequestBidPayment requestBidPayment, @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {
//        ResponseBidPayment responsebidPayment = paymentService.doBidPayment(requestBidPayment, memberEmail);

        return paymentService.doBidPayment(requestBidPayment, memberEmail);
    }

    // 결제 내역 조회
    @GetMapping("/paymentHistory")
    public ResponseEntity<CommonResponse> getPaymentHistory(@AuthenticationPrincipal(expression = "username") String memberEmail) {
//        List<ResponsePaymentHistory> responsePaymentHistoryList = paymentService.getPaymentHistory(memberEmail);
        return paymentService.getPaymentHistory(memberEmail);
    }
}
