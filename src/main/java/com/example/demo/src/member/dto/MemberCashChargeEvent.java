package com.example.demo.src.member.dto;

import lombok.Getter;

@Getter
public class MemberCashChargeEvent {
    private String email;
    private Long balance;

    public MemberCashChargeEvent(String email, Long balance) {
        this.email = email;
        this.balance = balance;
    }
}
