package com.example.demo.src.member.controller;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.member.dto.*;
import com.example.demo.src.member.service.MemberAuthService;
import com.example.demo.src.member.service.MemberAuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final MemberAuthServiceImpl memberAuthServiceImpl;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signUp(@Valid @RequestBody RequestSingUp registerDto) {
        if (registerDto.getAuthority().equals("멘토")) {
            return memberAuthService.mentorSignUp(registerDto);
        } else {
            return memberAuthService.menteeSignUp(registerDto);
        }
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody RequestLogin loginDto) {
        return memberAuthService.login(loginDto);
    }

    // 개인 정보 조회
    @GetMapping("/members")
    public ResponseEntity<CommonResponse> getMyInfo(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        return memberAuthService.getMyInfo(memberEmail);
    }

    @GetMapping("/checkName/{name}")
    public ResponseEntity<CommonResponse> checkDuplicatedName(@Valid @PathVariable String name){
        return memberAuthService.checkDuplicatedName(name);
    }

    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<CommonResponse> checkDuplicatedEmail(@Valid @PathVariable String email){
        return memberAuthService.checkDuplicatedEmail(email);
    }

    // 멤버 캐쉬 충전
    @PostMapping("/cashCharge")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> charge(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                                 @Valid @RequestBody RequestCashCharge chargeDto) {
        return memberAuthService.cashCharge(memberEmail, chargeDto);
    }

    // 멤버 정보 수정
    @PatchMapping("/modify")
    public ResponseEntity<CommonResponse> updateMember(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                                       @RequestBody RequestMemberPatch requestMemberPatch) {
        return memberAuthService.updateInfo(memberEmail, requestMemberPatch);
    }

    @PatchMapping("/deleteMember")
    public ResponseEntity<CommonResponse> deleteMember(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        return memberAuthService.deleteMember(memberEmail);
    }


    @PatchMapping("/updateProfileImg")
    public ResponseEntity<CommonResponse> updateProfileImg(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                           @Valid @RequestParam(value = "img") MultipartFile img) throws IOException{
        return memberAuthService.updateProfileImg(memberEmail, img);
    }

    @GetMapping("/notification")
    public ResponseEntity<CommonResponse> getNotification(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                          @AuthenticationPrincipal(expression = "authorities[0].authority") String authority){
        return memberAuthService.getNotification(memberEmail, authority);
    }
  
    @GetMapping("/alarmStatus")
    public ResponseEntity<CommonResponse> getAlarmStatus(@AuthenticationPrincipal(expression = "username") String memberEmail){
        return memberAuthService.getAlarmStatus(memberEmail);
    }

    @GetMapping("/sendSMS/{phone}")
    public BaseResponse<PostAuthCodeRes> sendSMS(@PathVariable String phone) {
        try {
            if (phone.length() != 11) {
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_UNREGEX_PHONE);
            }
            PostAuthCodeRes postAuthCodeRes = memberAuthServiceImpl.certifiedPhoneNumber(new PostAuthCodeReq(phone));

            return new BaseResponse<>(postAuthCodeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // Dummies for Test
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')") //인가 테스트
    public ResponseEntity<ResponseSignUp> getTokenTests(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(memberAuthServiceImpl.getTokenTests());
    }
}
