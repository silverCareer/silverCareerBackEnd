package com.example.demo.src.member.controller;

import com.example.demo.global.exception.CommonResponse;
import com.example.demo.global.security.CustomJwtFilter;
import com.example.demo.src.member.dto.*;
import com.example.demo.src.member.service.MemberAuthService;
import com.example.demo.utils.ValidationRegex;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/members")
    public ResponseEntity<CommonResponse> signUp(@Valid @RequestBody RequestSingUp registerDto) throws IllegalAccessException {
        if (registerDto.getAuthority().equals("멘토")) {
            memberAuthService.mentorSignUp(registerDto);
        } else if (registerDto.getAuthority().equals("멘티")) {
            memberAuthService.menteeSignUp(registerDto);
        }
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .build());
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
        System.out.println(user.getUsername() + " " + user.getAuthorities());
        return ResponseEntity.ok(memberAuthService.getTokenTests());
    }

    // 개인 정보 조회
    @GetMapping("/members")
    public ResponseEntity<ResponseMyInfo> getMyInfo(Authentication authentication) throws IllegalAccessException {
        String email = authentication.getName();

        return ResponseEntity.ok(memberAuthService.getMyInfo(email));
    }

    // 멤버 캐쉬 충전
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
        if(!memberPasswordPatchDto.getPassword().isEmpty() && memberPasswordPatchDto.getPassword() != null){
            memberAuthService.updatePassword(memberPasswordPatchDto, memberEmail);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 멤버 전화번호 수정
    @PatchMapping("/modifyPhoneNum")
    public ResponseEntity updateMemberPhoneNum(@RequestBody MemberPhonePatchDto memberPhonePatchDto,
                                               @AuthenticationPrincipal(expression = "username") String memberEmail) throws IllegalAccessException {
        String phoneNum = memberPhonePatchDto.getPhoneNum();
        if (phoneNum != null && !phoneNum.isEmpty()) {
            memberAuthService.updatePhoneNum(new MemberPhonePatchDto(phoneNum), memberEmail);
        }
        return ResponseEntity.ok().build();
    }
}
