package com.example.demo.src.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record ResponseUser (
        Long userIdx,
        String userName,
        String userEmail,
        String password,
        String userImage,
        Long userAge,
        String phoneNumber,
        String provider,
        String account,
        boolean activated
    // private String tokenWeight; //tokenWeight for refreshToken
) {

}

