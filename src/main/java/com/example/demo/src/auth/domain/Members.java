package com.example.demo.src.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Members {

    @Id
    @Column(name = "member_idx")
    private Long memberIdx;
    @Column(name = "email")
    private String userEmail;
    @Column(name = "password")
    private String userPassword;
    @Column(name = "name")
    private String userName;
    @Column(name = "phonenumber")
    private String userNumber;
    @Column(name = "age")
    private int userAge;
    @Column(name = "account_balance")
    private int userAccount;

    @Builder
    public Members(Long memberIdx, String userEmail, String userName, String userPassword, String userNumber,
                  int userAge, int userAccount) {
        this.memberIdx = memberIdx;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNumber = userNumber;
        this.userAge = userAge;
        this.userAccount = userAccount;
    }
}
