package com.example.demo.src.payment.dto;

import com.example.demo.src.payment.domain.Payment;
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
    private String paymentName;

    public static ResponsePaymentHistory of(Payment payment){
        return ResponsePaymentHistory.builder()
                .localDate(payment.getPaymentDate())
                .amount(payment.getPaymentAmount())
                .paymentName(payment.getPaymentName())
                .build();
    }
}
