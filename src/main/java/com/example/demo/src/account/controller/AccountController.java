
package com.example.demo.src.account.controller;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.account.dto.RequestAccountCharge;
import com.example.demo.src.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

//     계좌 잔액 충전
    @PostMapping("/accountCharge")
    public ResponseEntity charge(
            @Valid @RequestBody RequestAccountCharge chargeDto, @AuthenticationPrincipal(expression = "username") String memberEmail
    ) throws IllegalAccessException{
        Account account = accountService.charge(chargeDto, memberEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(Long.toString(account.getAccountIdx())));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}

