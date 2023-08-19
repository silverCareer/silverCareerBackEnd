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
    private Long productIdx;
    private String paymentType;
    private String mentorName;

    public static ResponsePaymentHistory of(Payment payment, String mentorName){
        return ResponsePaymentHistory.builder()
                .localDate(payment.getPaymentDate())
                .amount(payment.getPaymentAmount())
                .paymentName(payment.getPaymentName())
                .productIdx(payment.getProductIdx())
                .paymentType(payment.getPaymentType())
                .mentorName(mentorName)
                .build();
    }
}
