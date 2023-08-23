package com.example.demo.src.member.service;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.global.exception.error.charge.InvalidAmountException;
import com.example.demo.global.exception.error.member.*;
import com.example.demo.global.security.CustomJwtFilter;
import com.example.demo.global.security.RefreshTokenProvider;
import com.example.demo.global.security.TokenProvider;
import com.example.demo.global.S3Service;
import com.example.demo.src.account.domain.Account;
import com.example.demo.src.bid.domain.BidStatus;
import com.example.demo.src.bid.repository.BidRepository;
import com.example.demo.src.member.domain.AuthAdapter;
import com.example.demo.src.member.domain.Authority;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.dto.*;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.suggestion.domain.Suggestion;
import com.example.demo.src.suggestion.repository.SuggestionRepository;
import com.example.demo.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService{
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final SuggestionRepository suggestionRepository;
    private final BidRepository bidRepository;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final S3Service s3Service;

    @Value("${sns.service.api-key}")
    private String apiKey;

    @Value("${sns.service.api-secret-key}")
    private String apiSecretKey;

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> mentorSignUp(final RequestSingUp registerDto) {
        if(!isRegexEmail(registerDto.getEmail())){
            throw new WrongEmailInputException();
        }
        if(!isRegexPassword(registerDto.getPassword())){ // 비밀번호 정규식
            throw new WrongPasswordInputException();
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

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("회원가입 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> menteeSignUp(final RequestSingUp registerDto) {
        if(!isRegexEmail(registerDto.getEmail())){
            throw new WrongEmailInputException();
        }
        if(!isRegexPassword(registerDto.getPassword())){ // 비밀번호 정규식
            throw new WrongPasswordInputException();
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

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("회원가입 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> login(final RequestLogin loginDto) {
        String username = loginDto.getEmail();
        String password = loginDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Member member = memberRepository.findMemberByUsername(username);
        if(!member.isActivated()){
            throw new CustomException(ErrorCode.DELETED_MEMBER);
        }
        String accessToken = tokenProvider.createJwt(authentication);
        Long tokenWeight = ((AuthAdapter) authentication.getPrincipal()).getAuth().getTokenWeight();
        String refreshToken = refreshTokenProvider.createRefreshToken(authentication, tokenWeight);

        ResponseLogin response = ResponseLogin.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(member.getName())
                .authority(member.getAuthority().getAuthorityName())
                .balance(member.getBalance())
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + response.getAccessToken());

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> getMyInfo(final String memberEmail){
        ResponseMyInfo responseMyInfo = ResponseMyInfo.of(memberRepository.findByUsername(memberEmail)
                .orElseThrow(NotFoundMemberException::new));

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(responseMyInfo).build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> checkDuplicatedName(final String name){
        Member member = memberRepository.findByName(name).orElse(null);
        if(member != null){
            throw new DuplicateMemberNameException();
        }
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("닉네임 중복체크 성공").build());
    }

    // 캐시 충전
    @Override
    @Transactional
    public ResponseEntity<CommonResponse> cashCharge(final String memberEmail, final RequestCashCharge requestCashCharge) {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(NotFoundMemberException::new);
        long amount = requestCashCharge.getBalance();
        if (amount > 0) {
            memberAccountDeductEvent(memberEmail, amount);
        } else{
            throw new InvalidAmountException();
        }
        member.addCash(amount);
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("캐쉬 충전 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> updateInfo(final String memberEmail, final RequestMemberPatch requestMemberPatch) {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(NotFoundMemberException::new);

        String oldPassword = member.getPassword();
        String oldPhoneNum = member.getPhoneNumber();
        String newPassword = requestMemberPatch.getPassword();
        String newPhoneNum = requestMemberPatch.getPhoneNum();

        if(newPassword != null && !newPassword.isEmpty()){
            if (isRegexPassword(newPassword)) { // 비밀번호 정규식
                if (checkSamePassword(newPassword, oldPassword)) {
                    member.updatePassword(passwordEncoder.encode(newPassword));
                } else {
                    throw new DuplicateMemberPasswordException();
                }
            } else {
                throw new WrongPasswordInputException();
            }
        }
        if(newPhoneNum != null && !newPhoneNum.isEmpty()){
            newPhoneNum = makePhoneNum(newPhoneNum);// '-' 제거
            if (isRegexPhoneNum(newPhoneNum)) { // 전화번호 정규식
                if(checkSamePhoneNum(newPhoneNum, oldPhoneNum)){
                    member.updatePhoneNum(newPhoneNum);
                } else {
                    throw new DuplicateMemberPhoneNumException();
                }
            } else {
                throw new WrongPhoneNumInputException();
            }
        }
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("회원정보 변경 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> deleteMember(final String memberEmail){
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(NotFoundMemberException::new);
        member.memberActivationControl(false);

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("회원탈퇴 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> updateProfileImg(final String memberEmail, final MultipartFile img) throws IOException {
        Member member = memberRepository.findByUsername(memberEmail)
                .orElseThrow(NotFoundMemberException::new);
        String uploadedImgUrl = "";
        if(!img.isEmpty()){
            uploadedImgUrl = s3Service.upload(img, "profile", memberEmail);
        }
        member.updateProfileImg(uploadedImgUrl);

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("프로필 이미지 업데이트 성공").build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> getNotification(final String username, final String authority) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(NotFoundMemberException::new);
        String category = member.getCategory();
        Object res;

        if (authority.equals("ROLE_MENTOR")) {
            List<Suggestion> getFromSuggestions = suggestionRepository.findUnterminatedSuggestionsByCategory(category);

            List<Suggestion> terminatedSuggestions = suggestionRepository.findSuggestionsWithCompleteBidsAndMember(member);

            List<Suggestion> notifications = getFromSuggestions.stream()
                    .filter(suggestion -> !terminatedSuggestions.contains(suggestion))
                    .collect(Collectors.toList());

            Map<Suggestion, Boolean> notificationMap = new HashMap<>();
            notifications.forEach(suggestion -> notificationMap.put(suggestion, false));

            suggestionRepository.findSuggestionsWithInCompleteBidsAndMember(member)
                    .forEach(suggestion -> notificationMap.put(suggestion, true));

            res = notificationMap.entrySet().stream()
                    .sorted(Comparator.comparingLong(entry-> -entry.getKey().getSuggestionIdx()))
                    .map(entry -> MentorNotificationRes.of(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        } else {
            res = bidRepository.findBidBySuggestionMemberAndStatus(member, BidStatus.진행중)
                    .stream()
                    .map(MenteeNotificationRes::of)
                    .collect(Collectors.toList());
        }
        member.updateAlarmStatus(false);

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(res).build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> getAlarmStatus(final String username){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(NotFoundMemberException::new);

        String content = member.getAuthority().getAuthorityName().equals("ROLE_MENTOR")
                ? "새로운 의뢰가"
                : "새로운 입찰이";
        String msg = member.isCheckedAlarm() ? String.format("%s 도착했습니다.", content) : "";

        AlarmStatus response =  AlarmStatus.builder()
                .status(member.isCheckedAlarm())
                .message(msg)
                .build();

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }



    /******************************HELPER FUNCTION******************************/
    public void createAccountEvent(Account account, Member member){
        MemberCreateEvent memberCreateEvent = new MemberCreateEvent(member.getUsername(), account.getBankName(), account.getAccountNum(), account.getBalance());
        applicationEventPublisher.publishEvent(memberCreateEvent);
    }

    // 멤버 계좌 잔액 차감 이벤트 발생
    public void memberAccountDeductEvent(String email, Long balance) {
        MemberCashChargeEvent memberCashChargeEvent = new MemberCashChargeEvent(email, balance);
        applicationEventPublisher.publishEvent(memberCashChargeEvent);
    }

    // 전화번호 정규식
    public boolean isRegexPhoneNum(String phoneNum) {
        String regex = "^\\d{11}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    // 전화번호에서 '-'을 제거해주는 역할
    public String makePhoneNum(String phoneNum) {
        if (phoneNum.contains("-")) {
            phoneNum = phoneNum.replaceAll("-", "");
        }
        return phoneNum;
    }

    //  비밀번호 정규식
    /*  적어도 하나의 알파벳 (A-Za-z) 문자를 포함
        적어도 하나의 숫자 (0-9) 문자를 포함
        적어도 하나의 특수 문자 ($@$!%*#?&)를 포함
        오직 알파벳 문자, 숫자 문자 또는 지정된 특수 문자로만 구성
        길이는 8 ~ 20자 사이 */
    public boolean isRegexPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean isRegexEmail(String email){
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
            return new PostAuthCodeRes(numStr);
        } catch (CoolsmsException e) {
        } throw new BaseException(BaseResponseStatus.FAILED_TO_SEND_SNS_AUTH_CODE);
    }

    //Dummies for Test
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
}
