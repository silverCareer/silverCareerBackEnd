package com.example.demo.src.auth.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "auth")
@Getter
@NoArgsConstructor
public class Auth {

    @Id
    @Column(name = "auth_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authIdx;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "name", length = 50, unique = true)
    private String name;

    @Column(name = "phone_num")
    private String phoneNumber;

    @Column(name = "age")
    private Long age;

    @Column(name = "provider")
    private String provider;

    @Column(name = "activated") //계정 활성 여부?
    private boolean activated;

    @ManyToMany // JoinTable 어노테이션으로 인가 권한 테이블과 계정 인증 테이블과 조인한다.
    // 양 테이블 간의 연관 관계를 설정하는 code
    @JoinTable(
            name = "auth_authority",
            joinColumns = {@JoinColumn(name = "auth_idx", referencedColumnName ="auth_idx" )},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @Builder
    public Auth(String email, String password, String name, String phoneNumber, Set<Authority> authorities,
                Long age, String provider, boolean activated){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.activated = activated;
//        this.tokenWeight = 1L; 리프레시 토큰 가중치 설정
    }
}
