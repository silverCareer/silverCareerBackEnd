package com.example.demo.src.account.controller;

import com.example.demo.global.exception.BaseResponse;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.dto.ResponseAccountCharge;
import com.example.demo.src.account.provider.AccountProvider;
import com.example.demo.src.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;
    private final AccountProvider accountProvider;

    private AccountController(AccountService accountService, AccountProvider accountProvider){
        this.accountProvider = accountProvider;
        this.accountService = accountService;
    }

    // 계좌 잔액 충전
    @PostMapping("/charge")
    public BaseResponse<?> charge(
            @Valid @RequestBody RequestAccountCharge chargeDto
            ) throws IllegalAccessException{
        accountService.charge(chargeDto);
//        ResponseAccountCharge
        return new BaseResponse<>(HttpStatus.OK);
    }

}
