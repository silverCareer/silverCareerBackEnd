package com.example.demo.src.account.dto;

import com.example.demo.src.account.domain.Account;
import lombok.Builder;

@Builder
public class ResponseAccountCharge {
    Long accountIdx;
    String bankName;
    String accountNum;
    Long balance;

    public static ResponseAccountCharge of(Account account){
        if (account == null) return null;
        return ResponseAccountCharge.builder()
                .accountIdx(account.getAccountIdx())
                .bankName(account.getBankName())
                .accountNum(account.getAccountNum())
                .balance(account.getBalance())
                .build();
    }
}
