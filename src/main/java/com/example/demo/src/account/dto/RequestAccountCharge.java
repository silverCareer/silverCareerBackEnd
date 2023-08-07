package com.example.demo.src.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccountCharge {
    private String bankName;
    private String accountNum;
    private Long balance;
}
