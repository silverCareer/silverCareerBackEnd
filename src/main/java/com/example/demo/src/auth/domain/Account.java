package com.example.demo.src.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @Column(name = "account_idx")
    private Long accountIdx;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "account_num")
    private String accountNum;
    @Column
    private Long balance;

    //@OneToOne으로 연결
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_Idx")
    private Auth auth;
    public void setAuthInfo(Auth auth) {
        this.auth = auth;
        auth.changeAccount(this);
    }

    @Builder
    public Account(Long accountIdx, String bankName, String accountNum, Long balane) {
        this.accountIdx = accountIdx;
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balane;
    }
}
