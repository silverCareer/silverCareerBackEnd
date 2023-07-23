package com.example.demo.src.member;

import com.example.demo.global.exception.BaseException;
import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.domain.Authority;
import com.example.demo.src.auth.repository.AuthRepository;
import com.example.demo.utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class MemberService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    public MemberService(AuthRepository authRepository, PasswordEncoder passwordEncoder, SecurityUtil securityUtil) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtil = securityUtil;
    }

    @Transactional
    public ResponseMemberRegister signup(RequestMemberRegister registerDto) throws IllegalAccessException {
        if (authRepository.findOneWithAuthoritiesByUsername(registerDto.username()).orElseGet(() -> null) != null) {
            throw new IllegalAccessException("error");
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
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        // DB에 저장하고 그걸 DTO로 변환해서 반환, 예제라서 비번까지 다 보낸다. 원랜 당연히 보내면 안댐
        return ResponseMemberRegister.of(authRepository.save(user));
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
