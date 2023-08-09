package com.example.demo.src.member.controller;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.security.CustomJwtFilter;
import com.example.demo.src.member.dto.RequestLogin;
import com.example.demo.src.member.dto.RequestSingUp;
import com.example.demo.src.member.dto.ResponseLogin;
import com.example.demo.src.member.dto.ResponseSignUp;
import com.example.demo.src.member.Provider.MemberProvider;
import com.example.demo.src.member.dto.*;
import com.example.demo.src.member.service.MemberAuthService;
import com.example.demo.utils.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberAuthService memberAuthService;
    private final MemberProvider memberProvider;
    private final SecurityUtil securityUtil;



    @PostMapping("/members")
    public ResponseEntity<CommonResponse> signUp(@Valid @RequestBody RequestSingUp registerDto) throws IllegalAccessException {

        if (registerDto.getAuthority().equals("멘토")) {
            memberAuthService.mentorSignUp(registerDto);
        } else if (registerDto.getAuthority().equals("멘티")) {
            memberAuthService.menteeSignUp(registerDto);
        }
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("회원가입 성공")
                .build());
    }

    @GetMapping("/sendSMS/{phone}")
    public BaseResponse<PostAuthCodeRes> sendSMS(@PathVariable String phone) {
        try {
            if (phone.length() != 11) {
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_UNREGEX_PHONE);
            }

            System.out.println("수신자 번호 : " + phone);
            PostAuthCodeRes postAuthCodeRes = memberAuthService.certifiedPhoneNumber(new PostAuthCodeReq(phone));

            return new BaseResponse<>(postAuthCodeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody RequestLogin loginDto) {
        ResponseLogin responseDto = memberAuthService.login(loginDto.getEmail(), loginDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + responseDto.getAccessToken());

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(responseDto)
                .build();

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')") //인가 테스트
    public ResponseEntity<ResponseSignUp> getTokenTests(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(memberAuthService.getTokenTests());
    }

    @GetMapping("/members")
    public ResponseEntity<ResponseMyInfo> getMyInfo(Authentication authentication) throws IllegalAccessException {
        String email = authentication.getName();
        System.out.println(email);
//        ResponseMemberRegister responseMemberRegister = memberService.signUp(registerDto);

        return ResponseEntity.ok(memberProvider.getMyInfo(email));
    }

    @PostMapping("/cashCharge")
    public ResponseEntity<?> charge(
            @Valid @RequestBody RequestCashCharge chargeDto, @AuthenticationPrincipal(expression = "username") String memberEmail
    ) throws IllegalAccessException {
        memberAuthService.cashCharge(chargeDto, memberEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 멤버 비밀번호 수정
    @PatchMapping("/modifyPassword")
    public ResponseEntity updateMemberPassword(@RequestBody MemberPasswordPatchDto memberPasswordPatchDto,
                                       @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {

        memberAuthService.updatePassword(memberPasswordPatchDto, memberEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/modifyPhoneNum")
    public ResponseEntity updateMemberPhoneNum(@RequestBody MemberPhonePatchDto memberPhonePatchDto,
                                       @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {

        memberAuthService.updatePhoneNum(memberPhonePatchDto, memberEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }


//    @PatchMapping("/updateProfileImg")
//    public ResponseEntity updateProfileImg(@RequestParam(value="img")MultipartFile img) throws IOException{
//        return securityUtil.getCurrentUsername()
//                .map(username -> {
//                    try {
//                        memberAuthService.updateProfileImg(username, img);
//                        return new ResponseEntity<>(HttpStatus.OK);
//                    } catch (IOException e) {
//                        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
//                    }
//                })
//                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
//    }

}
