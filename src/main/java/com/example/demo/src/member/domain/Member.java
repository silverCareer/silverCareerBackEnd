package com.example.demo.src.member.domain;

import com.example.demo.src.account.domain.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "email", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "phone_num", nullable = false)
    private String phoneNumber;

    @Column(name = "age", nullable = false)
    private Long age;

    @Column(name = "user_image", columnDefinition = "TEXT")
    private String userImage;

    @Column(name = "career", length = 20)
    private String career;

    @Column(name = "category", length = 20)
    private String category;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "token_weight", nullable = false)
    private Long tokenWeight;

    @Column(name = "activated", nullable = false) //계정 활성 여부?
    private boolean activated;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "member")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="authority", referencedColumnName = "authority_name", nullable = false)
    private Authority authority;

    @Builder
    public Member(String email, String password, String name, String phoneNumber, Long age,
                  String userImage, String career, String category, Long balance, Long tokenWeight,
                  boolean activated, Account account, Authority authority){
        this.username = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.userImage = userImage;
        this.career = career;
        this.category = category;
        this.balance = balance;
        this.tokenWeight = 1L;
        this.activated = activated;
        this.account = account;
        this.authority = authority;
    }

    public void increaseTokenWeight(){
        this.tokenWeight++;
    }
}
