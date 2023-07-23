package com.example.demo.src.auth.dto;

import lombok.Builder;

@Builder
public record ResponseToken(
        String accessToken,
        String refreshToken
){
}
