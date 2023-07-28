package com.example.demo.src.account.domain;

import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.auth.domain.Auth;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "account_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountIdx;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_num")
    private String accountNum;

    @Column(name = "balance")
    private Long balance;

    @OneToOne
    @JoinColumn(name = "auth_idx")
    private Auth auth;

    @Builder
    public Account(Long accountIdx, String bankName, String accountNum, Long balance, Auth auth){
        this.accountIdx = accountIdx;
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balance;
        this.auth = auth;
    }


    public void addBalance(long amount) throws IllegalArgumentException {

        if (this.balance == null) {
            this.balance = amount;
        } else {
            this.balance += amount;
        }
    }
}
