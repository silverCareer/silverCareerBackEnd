package com.example.demo.global.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class JwtProperties {
    private String header = "Authorization"; //토큰 헤더 필드
    @Value("${spring.security.jwt.secret}")
    private String secret;
    @Value("${spring.security.jwt.refreshTokenSecret}")
    private String refreshTokenSecret;
    private long accessTokenValidityInSeconds = 1800; // 액세스토큰 유효기간 필드
    private long refreshTokenValidityInSeconds = 604800;// 리프레시토큰 유효기간 필드
}