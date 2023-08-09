package com.example.demo.src.member.service;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.DuplicatedMemberException;
import com.example.demo.global.security.RefreshTokenProvider;
import com.example.demo.global.security.TokenProvider;
import com.example.demo.src.S3Service;
import com.example.demo.src.account.domain.Account;
import com.example.demo.src.member.Provider.MemberProvider;
import com.example.demo.src.member.domain.AuthAdapter;
import com.example.demo.src.member.domain.Authority;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.dto.*;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final MemberProvider memberProvider;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final S3Service s3Service;


    @Value("${sns.service.api-key}")
    private String apiKey;


    @Value("${sns.service.api-secret-key}")
    private String apiSecretKey;

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
    public void cashCharge(RequestCashCharge requestCashCharge, String memberEmail) throws IllegalAccessException{
        Member member = memberRepository.findByUsername(memberEmail).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        long amount = requestCashCharge.getBalance();
        if(memberProvider.validateCash(amount)){
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

    @Transactional
    public void updatePassword(MemberPasswordPatchDto memberPasswordPatchDto, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        member.setPassword(passwordEncoder.encode(memberPasswordPatchDto.getPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public void updatePhoneNum(MemberPhonePatchDto memberPhonePatchDto, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail).orElseThrow(()
                -> new IllegalAccessException("해당 유저가 없습니다."));
        member.setPhoneNumber(memberPhonePatchDto.getPhoneNum());
        memberRepository.save(member);
    }

    @Transactional
    public void updateProfileImg(String username, MultipartFile img) throws IOException {
        Member member = memberRepository.findMemberByUsername(username);
        String uploadedImgUrl = "";
        if(!img.isEmpty()){
            uploadedImgUrl = s3Service.upload(img, "profile", username);
        }
        member.setUserImage(uploadedImgUrl);
        memberRepository.save(member);
    }

    public void createAccountEvent(Account account, Member member){
        MemberCreateEvent memberCreateEvent = new MemberCreateEvent(member.getUsername(), account.getBankName(), account.getAccountNum(), account.getBalance());
        applicationEventPublisher.publishEvent(memberCreateEvent);
    }

    public void memberAccountDeductEvent(String email, Long balance){
        MemberCashChargeEvent memberCashChargeEvent = new MemberCashChargeEvent(email, balance);
        applicationEventPublisher.publishEvent(memberCashChargeEvent);
    }


    public PostAuthCodeRes certifiedPhoneNumber(PostAuthCodeReq postAuthCodeReq) throws BaseException{

        String api_key = apiKey;
        String api_secret = apiSecretKey;
        Message coolsms = new Message(api_key, api_secret);


        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        String phoneNumber = postAuthCodeReq.getPhone();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);
        params.put("from", "01039319312");
        params.put("type", "SMS");
        params.put("text", "SilverCareer_회원가입 : 인증번호는" + "["+numStr+"]" + "입니다.");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());

            return new PostAuthCodeRes(numStr);
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        } throw new BaseException(BaseResponseStatus.FAILED_TO_SEND_SNS_AUTH_CODE);
    }
}
