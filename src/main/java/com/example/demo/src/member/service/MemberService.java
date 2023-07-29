package com.example.demo.src.member.service;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.provider.AccountProvider;
import com.example.demo.src.account.repository.AccountRepository;
import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.domain.Authority;
import com.example.demo.src.auth.repository.AuthRepository;
import com.example.demo.src.member.dto.RequestCashCharge;
import com.example.demo.src.member.dto.RequestMemberRegister;
import com.example.demo.src.member.dto.ResponseMemberRegister;
import com.example.demo.src.member.provider.MemberProvider;
import com.example.demo.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final MemberProvider memberProvider;
    private final AccountRepository accountRepository;
    private final AccountProvider accountProvider;

    @Transactional
    public ResponseMemberRegister signUp(RequestMemberRegister registerDto) throws IllegalAccessException {
        if (authRepository.findOneWithAuthoritiesByUsername(registerDto.username()).orElseGet(() -> null) != null) {
            throw new IllegalAccessException("이름이 중복됩니다.");
        }

        // 이 유저는 권한이 ROLE_USER
        // 이건 부팅 시 data.sql에서 INSERT로 디비에 반영한다. 즉 디비에 존재하는 값이여야함
        Authority authority = Authority.builder()
                .authorityName("ROLE_MEMBER")
                .build();

        Auth user = Auth.builder()
                .username(registerDto.username())
                .password(passwordEncoder.encode(registerDto.password()))
                .email(registerDto.email())
                .age(registerDto.age())
                .provider(registerDto.provider())
                .phoneNumber(registerDto.phoneNumber())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .cash(0L)
                .build();

        // DB에 저장하고 그걸 DTO로 변환해서 반환, 예제라서 비번까지 다 보낸다. 원랜 당연히 보내면 안댐
        return ResponseMemberRegister.of(authRepository.save(user));
    }

    @Transactional
    public void cashCharge(RequestCashCharge requestCashCharge) throws IllegalAccessException{
        // requestCashCharge 객체에서 authIdx() 메서드를 호출하여 해당 인덱스를 가져와서, authRepository를 통해 Auth 객체를 조회
        Auth auth = authRepository.findById(requestCashCharge.authIdx()).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));

        // auth에서 authidx로 account 테이블의 잔액 조회
        Account account = accountRepository.findByAuth_AuthIdx(auth.getAuthIdx()).orElseThrow(() ->
                new IllegalAccessException("해당 유저의 계정 정보를 찾을 수 없습니다.")
        );

        long amount = requestCashCharge.cash();

        if(memberProvider.validateCash(amount)){ // 금액이 0 이상의 정수인지 판단
            if(accountProvider.validateAccountBalance(amount, account.getBalance())) { // 잔액 확인(잔액보다 충전하려는 금액이 더 크면 예외처리)
                auth.addCash(amount);
                account.minusBalance(amount);
            }
        }

        authRepository.save(auth);
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public ResponseMemberRegister getUserWithAuthorities(String username) {
        return ResponseMemberRegister.of(authRepository.findOneWithAuthoritiesByUsername(username).orElseGet(()->null));
    }

    // 현재 시큐리티 컨텍스트에 저장된 username에 해당하는 정보를 가져온다.
    @Transactional(readOnly = true)
    public ResponseMemberRegister getMyUserWithAuthorities() {
        return ResponseMemberRegister.of(securityUtil.getCurrentUsername().flatMap(authRepository::findOneWithAuthoritiesByUsername).orElseGet(()->null));
    }
}
