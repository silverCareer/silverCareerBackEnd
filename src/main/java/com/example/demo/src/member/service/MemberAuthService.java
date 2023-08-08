package com.example.demo.src.member.service;

import com.example.demo.global.security.RefreshTokenProvider;
import com.example.demo.global.security.TokenProvider;
import com.example.demo.src.account.domain.Account;
import com.example.demo.src.member.domain.AuthAdapter;
import com.example.demo.src.member.domain.Authority;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.dto.*;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public void mentorSignUp(final RequestSingUp registerDto) throws IllegalAccessException {
        if (memberRepository.findOneWithAuthorityByUsername(registerDto.getEmail()).orElseGet(() -> null) != null) {
            throw new IllegalAccessException("이미 가입된 사용자입니다!");
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
    public void menteeSignUp(final RequestSingUp registerDto) throws IllegalAccessException {
        if (memberRepository.findOneWithAuthorityByUsername(registerDto.getEmail()).orElseGet(() -> null) != null) {
            throw new IllegalAccessException("이미 가입된 사용자입니다!");
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

    // 캐시 충전
    @Transactional
    public void cashCharge(RequestCashCharge requestCashCharge, String memberEmail) throws IllegalAccessException{
        Member member = memberRepository.findByUsername(memberEmail).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        long amount = requestCashCharge.getBalance();
        if(validateCash(amount)){
            memberAccountDeductEvent(memberEmail, amount);
        } else {
            throw new IllegalAccessException("양수만 입력 가능합니다.");
        }
        member.addCash(amount);
        memberRepository.save(member);
    }

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

    // 멤버 비밀번호 수정
    @Transactional
    public void updatePassword(MemberPasswordPatchDto memberPasswordPatchDto, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        member.setPassword(passwordEncoder.encode(memberPasswordPatchDto.getPassword()));
        memberRepository.save(member);
    }

    // 멤버 전화번호 수정
    @Transactional
    public void updatePhoneNum(MemberPhonePatchDto memberPhonePatchDto, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        String phoneNum = memberPhonePatchDto.getPhoneNum();
        if(isRegexPhoneNum(phoneNum)) { // 전화번호 정규식
            phoneNum = makePhoneNum(phoneNum); // '-' 제거
        } else {
            throw new IllegalAccessException("잘못된 전화번호 입력 형식입니다.");
        }
        member.setPhoneNumber(phoneNum);
        memberRepository.save(member);
    }

    // 멤버 개인 조회
    public ResponseMyInfo getMyInfo(String memberName) throws IllegalAccessException {
        ResponseMyInfo responseMyInfo = ResponseMyInfo.of(memberRepository.findByUsername(memberName).orElseThrow(()
                -> new IllegalAccessException("해당 정보가 없습니다.")));

        return responseMyInfo;
    }

    // 계좌 생성 이벤트 발생
    public void createAccountEvent(Account account, Member member){
        MemberCreateEvent memberCreateEvent = new MemberCreateEvent(member.getUsername(), account.getBankName(), account.getAccountNum(), account.getBalance());
        applicationEventPublisher.publishEvent(memberCreateEvent);
    }

    // 멤버 계좌 잔액 차감 이벤트 발생
    public void memberAccountDeductEvent(String email, Long balance){
        MemberCashChargeEvent memberCashChargeEvent = new MemberCashChargeEvent(email, balance);
        applicationEventPublisher.publishEvent(memberCashChargeEvent);
    }

    // 충전하려는 금액 양의 정수인지 확인
    public boolean validateCash(long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("0원 이하는 충전이 불가능합니다.");
        }
        return true;
    }

    // 전화번호 정규식
    public static boolean isRegexPhoneNum(String phoneNum){
        String regex = "^[0-9-]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    // 전화번호에서 '-'을 제거해주는 역할
    public static String makePhoneNum(String phoneNum){
        if(phoneNum.contains("-")){
            phoneNum = phoneNum.replaceAll("-", "");
        }
        return phoneNum;
    }
}
