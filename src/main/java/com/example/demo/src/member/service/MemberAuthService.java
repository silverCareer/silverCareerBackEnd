package com.example.demo.src.member.service;

import com.example.demo.global.exception.error.DuplicatedMemberException;
import com.example.demo.global.security.RefreshTokenProvider;
import com.example.demo.global.security.TokenProvider;
import com.example.demo.src.account.domain.Account;
import com.example.demo.src.member.domain.AuthAdapter;
import com.example.demo.src.member.domain.Authority;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.dto.MemberCreateEvent;
import com.example.demo.src.member.dto.RequestSingUp;
import com.example.demo.src.member.dto.ResponseLogin;
import com.example.demo.src.member.dto.ResponseSignUp;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ResponseLogin login(final String username, final String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = tokenProvider.createJwt(authentication);

        Long tokenWeight = ((AuthAdapter) authentication.getPrincipal()).getAuth().getTokenWeight();
        String refreshToken = refreshTokenProvider.createRefreshToken(authentication, tokenWeight);

        return ResponseLogin.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void mentorSignUp(final RequestSingUp registerDto) {
        if (memberRepository.findOneWithAuthorityByUsername(registerDto.getEmail()).orElseGet(() -> null) != null) {
            throw new DuplicatedMemberException();
        }
        Authority authority = Authority.builder()
                .authorityName("ROLE_MENTOR")
                .build();

        Member member = Member.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .phoneNumber(registerDto.getPhoneNumber())
                .age(registerDto.getAge())
                .category(registerDto.getCategory())
                .activated(true)
                .authority(authority)
                .build();
        memberRepository.save(member);

        Account account = Account.builder()
                .accountNum(registerDto.getAccountNum())
                .bankName(registerDto.getBankName())
                .build();
    }

    @Transactional
    public void menteeSignUp(final RequestSingUp registerDto) {
        if (memberRepository.findOneWithAuthorityByUsername(registerDto.getEmail()).orElseGet(() -> null) != null) {
            throw new DuplicatedMemberException();
        }
        Authority authority = Authority.builder()
                .authorityName("ROLE_MENTEE")
                .build();
        Member member = Member.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .phoneNumber(registerDto.getPhoneNumber())
                .age(registerDto.getAge())
                .balance(0L)
                .authority(authority)
                .activated(true)
                .build();
        memberRepository.save(member);

        Account account = Account.builder()
                .accountNum(registerDto.getAccountNum())
                .bankName(registerDto.getBankName())
                .build();
        createAccountEvent(account, member);
    }

//    @Transactional(readOnly = true)
//    public void refreshToken(String refreshToken){
//
//    }

    @Transactional
    public ResponseSignUp getTokenTests() {
        return securityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthorityByUsername)
                .map(account -> ResponseSignUp.builder()
                        .username(account.getUsername())
                        .password(account.getPassword())
                        .name(account.getName())
                        .authority(account.getAuthority())
                        .build())
                .orElse(null);
    }

    public void createAccountEvent(Account account, Member member) {
        MemberCreateEvent memberCreateEvent = new MemberCreateEvent(member.getUsername(), account.getBankName(), account.getAccountNum(), account.getBalance());
        applicationEventPublisher.publishEvent(memberCreateEvent);
    }
}
