
package com.example.demo.src.account.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 계좌 잔액 충전
    @PostMapping("/accountCharge")
    public ResponseEntity<CommonResponse> charge(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                                 @Valid @RequestBody RequestAccountCharge chargeDto) {
        return accountService.charge(chargeDto, memberEmail);
    }
}

