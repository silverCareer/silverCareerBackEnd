package com.example.demo.src.kakao.dto;

import lombok.Builder;
@Builder
public record KakaoUser (
        Long userIdx,
        String userEmail,
        String password,
        String userImage,
        String provider,
        boolean activated
        // private String tokenWeight; //tokenWeight for refreshToken
) {

}
