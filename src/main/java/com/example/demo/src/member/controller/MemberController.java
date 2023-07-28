package com.example.demo.src.member.controller;

import com.example.demo.global.exception.BaseResponse;
import com.example.demo.src.member.dto.ResponseMyInfo;
import com.example.demo.src.member.provider.MemberProvider;
import com.example.demo.src.member.service.MemberService;
import com.example.demo.src.member.dto.RequestMemberRegister;
import com.example.demo.src.member.dto.ResponseMemberRegister;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final MemberProvider memberProvider;

    public MemberController(MemberService memberService, MemberProvider memberProvider) {
        this.memberService = memberService;
        this.memberProvider = memberProvider;
    }

    // user 등록 API
    @PostMapping("/members")
    public BaseResponse<ResponseMemberRegister> signUp(
            @Valid @RequestBody RequestMemberRegister registerDto
    ) throws IllegalAccessException {
        ResponseMemberRegister responseMemberRegister = memberService.signUp(registerDto);

        return new BaseResponse<>(responseMemberRegister);
    }

    @GetMapping("/members")
    public BaseResponse<ResponseMyInfo> getMyInfo(Authentication authentication) throws IllegalAccessException {
        String memberName = authentication.getName();
        System.out.println(memberName);
//        ResponseMemberRegister responseMemberRegister = memberService.signUp(registerDto);
        return new BaseResponse<>(memberProvider.getMyInfo(memberName));
    }
}
