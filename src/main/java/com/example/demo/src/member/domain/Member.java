package com.example.demo.src.member.domain;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.bid.domain.Bid;
import com.example.demo.src.suggestion.domain.Suggestion;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "name", length = 50, unique = true, nullable = false)
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

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "activated", nullable = false) //계정 활성 여부?
    private boolean activated;

    @Column(name = "checkedAlarm", columnDefinition = "BOOL DEFAULT false")
    private boolean checkedAlarm;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "member")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="authority", referencedColumnName = "authority_name", nullable = false)
    private Authority authority;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Suggestion> suggestionList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Bid> bidList = new ArrayList<>();

    @Builder
    public Member(String email, String password, String name, String phoneNumber, Long age,
                  String userImage, String career, String category, Long balance, Long tokenWeight,
                  boolean activated, boolean checkedAlarm, Account account, Authority authority, String refreshToken){
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
        this.refreshToken = refreshToken;
        this.activated = activated;
        this.account = account;
        this.authority = authority;
        this.checkedAlarm = checkedAlarm;
    }

    public void increaseTokenWeight(){
        this.tokenWeight++;
    }

    public void registerRefresh(String refreshToken){
        this.refreshToken = refreshToken;
    }
    // 캐쉬 충전
    public void addCash(long amount) throws IllegalArgumentException {
        this.balance += amount;
    }

    public void deductCash(long amount) throws IllegalAccessException{
        this.balance -= amount;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void memberActivationControl(Boolean activated){
        this.activated = activated;
    }

    public void updatePhoneNum(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateProfileImg(String imgUrl){
        this.userImage = imgUrl;
    }

    public void addSuggestion(Suggestion suggestion){
        this.suggestionList.add(suggestion);
    }

    public void addBid(Bid bid) {
        this.bidList.add(bid);
    }

    public void updateAlarmStatus(boolean status){
        this.checkedAlarm = status;
    }
}
