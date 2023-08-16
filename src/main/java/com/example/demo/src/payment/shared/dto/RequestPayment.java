package com.example.demo.src.payment.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestPayment {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;
    private String paymentName;

    public static RequestPayment withCurrentTime(String paymentName) {
        return RequestPayment.builder()
                .localDate(LocalDate.now())
                .paymentName(paymentName)
                .build();
    }
}
