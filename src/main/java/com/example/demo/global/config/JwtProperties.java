package com.example.demo.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties (prefix = "jwt")
public class JwtProperties {
    private String header; //토큰 헤더 필드
    private String secret; //토큰 시크릿 스트링 필드
    private Long accessTokenValidityInSeconds; // 액세스토큰 유효기간 필드
}
