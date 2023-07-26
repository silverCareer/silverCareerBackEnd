package com.example.demo.src.kakao.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoOauth {
    private String accessToken;
    private String refreshToken;
    private int refreshValidTime;

    public void updateRefreshToken(String newToken) {
        this.refreshToken = newToken;
    }
    @Builder
    public KakaoOauth(String accessToken, String refreshToken, int refreshValidTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshValidTime = refreshValidTime;
    }
}
