//package com.example.demo.src.kakao;
//
//import com.example.demo.src.member.dto.ResponseToken;
//import com.example.demo.src.kakao.dto.KakaoUser;
//import lombok.Builder;
//
//@Builder
//public record ResponseLoginRegister(
//        Long userId,
//        String userEmail,
//        String userImage,
//        String provider,
//        String jwtAccessToken,
//        String jwtRefreshToken
//) {
//    public static ResponseLoginRegister of(KakaoUser user, ResponseToken token) {
//        if (user == null) return null;
//        return ResponseLoginRegister.builder()
//                .userId(user.userIdx())
//                .userEmail(user.userEmail())
//                .userImage(user.userImage())
//                .provider(user.provider())
//                .jwtAccessToken(token.accessToken())
//                .jwtRefreshToken(token.refreshToken())
//                .build();
//    }
//}
