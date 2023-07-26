package com.example.demo.src.kakao;

import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.dto.ResponseToken;
import com.example.demo.src.auth.domain.SocialUser;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record ResponseLoginRegister(
        Long userId,
        String userEmail,
        String userImage,
        String userAgeRange,
        String userBirth,
        String kakaoToken,
        String jwtAccessToken,
        String jwtRefreshToken
) {
    public static ResponseLoginRegister of(SocialUser user, ResponseToken token) {
        if (user == null) return null;
        return ResponseLoginRegister.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userImage(user.getUserImage())
                .userAgeRange(user.getUserAgeRange())
                .userBirth(user.getUserBirth())
                .jwtAccessToken(token.accessToken())
                .jwtRefreshToken(token.refreshToken())
                .build();
    }
}
