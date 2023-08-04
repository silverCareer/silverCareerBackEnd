package com.example.demo.src.account.domain;
import com.example.demo.src.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_idx")
    private Long accountIdx;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_num")
    private String accountNum;

    @Column(name = "balance")
    private Long balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

    @Builder
    public Account(Long accountIdx, String bankName, String accountNum, Long balance, Member member) {
        this.accountIdx = accountIdx;
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balance;
        this.member = member;
    }

    public void addBalance(long amount) throws IllegalArgumentException {
        if (this.balance == null) {
            this.balance = amount;
        } else {
            this.balance += amount;
        }
    }

    public void minusBalance(long amount) throws IllegalArgumentException {
        this.balance -= amount;
    }
}
