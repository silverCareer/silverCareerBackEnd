package com.example.demo.src.member.dto;

import lombok.Getter;

@Getter
public class MemberCreateEvent {

    private String username;
    private String bankName;
    private String accountNum;
    private Long balance = 0L;

    public MemberCreateEvent(String username, String bankName, String accountNum, Long balance){
        this.username = username;
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balance;
    }
}
