package com.example.demo.src.account.domain;
import com.example.demo.src.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("0")
    private Long balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private Member member;

    @Builder
    public Account(Long accountIdx, String bankName, String accountNum, Long balance, Member member) {
        this.accountIdx = accountIdx;
        this.bankName = bankName;
        this.accountNum = accountNum;
        this.balance = balance;
        this.member = member;
    }

    // 계좌 충전
    public void addBalance(long amount) throws IllegalArgumentException {
        if (this.balance != null){
            this.balance += amount;
        }
    }

    // 계좌 잔액 차감
    public void minusBalance(long amount) throws IllegalArgumentException {
        this.balance -= amount;
    }
}
