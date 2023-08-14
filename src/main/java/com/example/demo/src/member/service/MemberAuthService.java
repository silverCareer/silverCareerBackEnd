package com.example.demo.src.member.service;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.global.security.RefreshTokenProvider;
import com.example.demo.global.security.TokenProvider;
import com.example.demo.src.S3Service;
import com.example.demo.src.account.domain.Account;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        Member member = memberRepository.findMemberByUsername(username);

        return ResponseLogin.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(member.getName())
                .build();
    }

    @Transactional
    public void mentorSignUp(final RequestSingUp registerDto) throws IllegalAccessException {
        if (memberRepository.findOneWithAuthorityByUsername(registerDto.getEmail()).orElseGet(() -> null) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER_EXCEPTION);
        }
        if(!isRegexEmail(registerDto.getEmail())){
            throw new CustomException(ErrorCode.WRONG_EMAIL_INPUT);
        }
        if(!isRegexPassword(registerDto.getPassword())){ // 비밀번호 정규식
            throw new CustomException(ErrorCode.WRONG_PASSWORD_INPUT);
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
                .career(registerDto.getCareer())
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
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER_EXCEPTION);
        }
        if(!isRegexEmail(registerDto.getEmail())){
            throw new CustomException(ErrorCode.WRONG_EMAIL_INPUT);
        }
        if(!isRegexPassword(registerDto.getPassword())){ // 비밀번호 정규식
            throw new CustomException(ErrorCode.WRONG_PASSWORD_INPUT);
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
    public void cashCharge(RequestCashCharge requestCashCharge, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findByUsername(memberEmail).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        long amount = requestCashCharge.getBalance();
        if (validateCash(amount)) {
            memberAccountDeductEvent(memberEmail, amount);
        } else {
            throw new CustomException(ErrorCode.UNDER_ZERO_AMOUNT);
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
    public void updateInfo(RequestMemberPatch requestMemberPatch, String memberEmail) throws IllegalAccessException {
        Member member = memberRepository.findMemberByUsername(memberEmail);
        String oldPassword = getMyInfo(memberEmail).getPassword();
        String oldPhoneNum = getMyInfo(memberEmail).getPhoneNumber();
        String newPassword = requestMemberPatch.getPassword();
        String newPhoneNum = requestMemberPatch.getPhoneNum();

        if(newPassword != null && !newPassword.isEmpty()){
            if (isRegexPassword(newPassword)) { // 비밀번호 정규식
                if (checkSamePassword(newPassword, oldPassword)) {
                    member.updatePassword(passwordEncoder.encode(newPassword));
                } else {
                    throw new CustomException(ErrorCode.DUPLICATE_MEMBER_PASSWORD);
                }
            } else {
                throw new CustomException(ErrorCode.WRONG_PASSWORD_INPUT);
            }
        }
        if(newPhoneNum != null && !newPhoneNum.isEmpty()){
            if (isRegexPhoneNum(newPhoneNum)) {// 전화번호 정규식
                newPhoneNum = makePhoneNum(newPhoneNum); // '-' 제거
                if(checkSamePhoneNum(newPhoneNum, oldPhoneNum)){
                    member.updatePhoneNum(newPhoneNum);
                } else {
                    throw new CustomException(ErrorCode.DUPLICATE_MEMBER_PHONE_NUM);
                }
            } else {
                throw new CustomException(ErrorCode.WRONG_PHONE_NUM_INPUT);
            }
        }
        memberRepository.save(member);
    }


    public ResponseMyInfo getMyInfo(String memberName) throws IllegalAccessException {
        ResponseMyInfo responseMyInfo = ResponseMyInfo.of(memberRepository.findByUsername(memberName).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT)));

        return responseMyInfo;
    }

    @Transactional
    public void updateProfileImg(String username, MultipartFile img) throws IOException {
        Member member = memberRepository.findMemberByUsername(username);
        String uploadedImgUrl = "";
        if(!img.isEmpty()){
            uploadedImgUrl = s3Service.upload(img, "profile", username);
        }
        member.updateProfileImg(uploadedImgUrl);
        memberRepository.save(member);
    }

    public void createAccountEvent(Account account, Member member){
        MemberCreateEvent memberCreateEvent = new MemberCreateEvent(member.getUsername(), account.getBankName(), account.getAccountNum(), account.getBalance());
        applicationEventPublisher.publishEvent(memberCreateEvent);
    }

    // 멤버 계좌 잔액 차감 이벤트 발생
    public void memberAccountDeductEvent(String email, Long balance) {
        MemberCashChargeEvent memberCashChargeEvent = new MemberCashChargeEvent(email, balance);
        applicationEventPublisher.publishEvent(memberCashChargeEvent);
    }

    // 충전하려는 금액 양의 정수인지 확인
    public boolean validateCash(long amount) {
        if (amount <= 0) {
            throw new CustomException(ErrorCode.UNDER_ZERO_AMOUNT);
        }
        return true;
    }

    // 전화번호 정규식
    public static boolean isRegexPhoneNum(String phoneNum) {
        String regex = "^[0-9-]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    // 전화번호에서 '-'을 제거해주는 역할
    public static String makePhoneNum(String phoneNum) {
        if (phoneNum.contains("-")) {
            phoneNum = phoneNum.replaceAll("-", "");
        }
        return phoneNum;
    }

    //    비밀번호 정규식
//    적어도 하나의 알파벳 (A-Za-z) 문자를 포함
//    적어도 하나의 숫자 (0-9) 문자를 포함
//    적어도 하나의 특수 문자 ($@$!%*#?&)를 포함
//    오직 알파벳 문자, 숫자 문자 또는 지정된 특수 문자로만 구성
//    길이는 8 ~ 20자 사이
    public static boolean isRegexPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isRegexEmail(String email){
        String regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }


    // 과거 비밀번호와 현재 비밀번호 같은지 판별
    public boolean checkSamePassword(String newPassword, String oldPassword) {
        if (passwordEncoder.matches(newPassword, oldPassword)) {
            return false;
        } else {
            return true;
        }
    }

    // 과거 전화번호와 현재 전화번호 같은지 판별
    public boolean checkSamePhoneNum(String newPhoneNum, String oldPhoneNum) {
        if (newPhoneNum.equals(oldPhoneNum)) {
            return false;
        } else {
            return true;
        }
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
