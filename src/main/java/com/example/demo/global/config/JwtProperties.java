package com.example.demo.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class JwtProperties {
    private String header = "Authorization"; //토큰 헤더 필드
    private String secret ="c2lsdmVyQ2FyZWVyLXRlY2gtc3ByaW5nLWJvb3Qtand0LXNlY3JldC1zcHJpbmctc2VjdXJpdHktdGVjaC1zcHJpbmctYm9vdC1qd3QtdHV0b3JpYWwtc2VjcmV0"; //토큰 시크릿 스트링 필드
    private String refreshTokenSecret = "d29ybGQgbXkgbmFtZSBpcyBzc29uZyBoYWhhaGFoYWhhaGFoYSBydXJ1cnVydXJ1cnVydXJ1ZHNmc2Rmc2Rmc2Rmc2FkCg==";
    private long accessTokenValidityInSeconds = 600L; // 액세스토큰 유효기간 필드
    private long refreshTokenValidityInSeconds = 86400;// 리프레시토큰 유효기간 필드
}