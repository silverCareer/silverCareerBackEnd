package com.example.demo.src.member;

import com.example.demo.global.exception.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // user 등록 API
    @PostMapping("/members")
    public BaseResponse<ResponseMemberRegister> signUp(
            @Valid @RequestBody RequestMemberRegister registerDto
    ) throws IllegalAccessException {
        ResponseMemberRegister responseMemberRegister = memberService.signUp(registerDto);


        return new BaseResponse<>(responseMemberRegister);
    }
}
