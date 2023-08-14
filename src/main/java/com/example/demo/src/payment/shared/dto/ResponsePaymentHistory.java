package com.example.demo.src.payment.shared.dto;

import com.example.demo.src.payment.shared.domain.Payment;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
public class ResponsePaymentHistory {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;
    private Long amount;
    private String productName;

    public static ResponsePaymentHistory of(Payment payment){
        return ResponsePaymentHistory.builder()
                .localDate(payment.getPaymentDate())
                .amount(payment.getPaymentAmount())
                .productName(payment.getProduct().getProductName())
                .build();
    }
}
