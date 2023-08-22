package com.example.demo.src.payment.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.payment.dto.RequestBidPayment;
import com.example.demo.src.payment.dto.RequestPayment;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<CommonResponse> doProductPayment(RequestPayment requestPayment, String memberEmail) throws IllegalAccessException;

    ResponseEntity<CommonResponse> doBidPayment(RequestBidPayment requestBidPayment, String memberEmail) throws IllegalAccessException;

    ResponseEntity<CommonResponse> getPaymentHistory(String memberEmail);
}
