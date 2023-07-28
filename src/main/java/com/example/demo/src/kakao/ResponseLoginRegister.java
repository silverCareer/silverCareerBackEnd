package com.example.demo.src.kakao;

import com.example.demo.src.auth.dto.ResponseToken;
import com.example.demo.src.kakao.dto.ResponseUser;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record ResponseLoginRegister(
        Long userId,
        String userEmail,
        String userImage,
        Long age,
        String provider,
        String jwtAccessToken,
        String jwtRefreshToken
) {
    public static ResponseLoginRegister of(ResponseUser user, ResponseToken token) {
        if (user == null) return null;
        return ResponseLoginRegister.builder()
                .userId(user.userIdx())
                .userEmail(user.userEmail())
                .userImage(user.userImage())
                .age(user.age())
                .provider(user.provider())
                .jwtAccessToken(token.accessToken())
                .jwtRefreshToken(token.refreshToken())
                .build();
    }
}
