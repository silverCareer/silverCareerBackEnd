package com.example.demo.src.payment.shared.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.payment.shared.dto.RequestPayment;
import com.example.demo.src.payment.shared.dto.ResponsePayment;
import com.example.demo.src.payment.shared.dto.ResponsePaymentHistory;
import com.example.demo.src.payment.shared.service.PaymentService;
import jakarta.validation.Valid;
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

    @PostMapping("/pay")
    public ResponseEntity<ResponsePayment> pay(@RequestBody RequestPayment requestPayment, @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {
        ResponsePayment responsePayment = paymentService.doPayment(requestPayment, memberEmail);

        return new ResponseEntity<>(responsePayment, HttpStatus.OK);
    }

    @GetMapping("/paymentHistory")
    public ResponseEntity<CommonResponse> getPaymentHistory(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        List<ResponsePaymentHistory> responsePaymentHistoryList = paymentService.getPaymentHistory(memberEmail);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(responsePaymentHistoryList)
                .build());
    }
}
