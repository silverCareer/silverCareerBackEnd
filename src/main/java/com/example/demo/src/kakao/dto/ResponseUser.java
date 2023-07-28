package com.example.demo.src.kakao.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record ResponseUser (

    Long userIdx,
    String userEmail,
    String password,
    String userName,
    String userImage,
    Long age, //type미정
    String phoneNumber, //type미정
    String provider
    // private String tokenWeight; //tokenWeight for refreshToken
) {

}

