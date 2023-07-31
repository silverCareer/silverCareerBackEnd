package com.example.demo.src.auth.domain;


import com.example.demo.src.account.domain.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.Set;

@Entity
@Table(name = "auth")
@Getter
@NoArgsConstructor
public class Auth {

    @Id
    @Column(name = "auth_idx")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authIdx;

    @Column(name = "user_image")
    private String userImage;
    @Column(name = "username", length = 50, unique = true)
    private String username;
    @Column(name = "password", length = 100)
    private String password;
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "activated") //계정 활성 여부?
    private boolean activated;

    @Column(name = "token_weight")
    private Long tokenWeight;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "age")
    private Long age;

    @Column(name = "provider")
    private String provider;

//    @Column(name = "cash")
//    private Long cash;

    @OneToOne(mappedBy = "auth")
    private Account account;

    public void changeAccount(Account account) {
        this.account = account;
    }

    @ManyToMany // JoinTable 어노테이션으로 인가 권한 테이블과 계정 인증 테이블과 조인한다.
    // 양 테이블 간의 연관 관계를 설정하는 code
    @JoinTable(
            name = "auth_authority",
            joinColumns = {@JoinColumn(name = "auth_idx", referencedColumnName ="auth_idx" )},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @Builder
    public Auth(String email, String password, String username, String phoneNumber, Set<Authority> authorities,
                Long age, String provider, boolean activated, Long cash){
        this.authIdx = 1L;
        this.email = email;
        this.password = password;
        this.username = username;
        this.age = age;
        this.userImage = userImage;
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.activated = activated;
        this.tokenWeight = 1L; // 리프레시 토큰 가중치 설정 -> Admin이 Member에 대한 리프레시 토큰 유효하지 않을때 검증 취소
//        this.cash = cash;
    }

    public void addCash(long amount) throws IllegalArgumentException {

//        if (this.cash == null) {
//            this.cash = 0L;
//        } else {
//            this.cash += amount;
//        }
    }

    public void increaseTokenWeight(){
        this.tokenWeight++;
    }
}
