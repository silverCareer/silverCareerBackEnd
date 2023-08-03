//package com.example.demo.src.kakao;
//
//import com.example.demo.global.security.RefreshTokenProvider;
//import com.example.demo.global.security.TokenProvider;
//import com.example.demo.src.member.dto.ResponseToken;
//import com.example.demo.src.member.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KakaoMemberServiceImpl implements MemberService {
//
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenProvider refreshTokenProvider;
//
//    public ResponseToken authenticate(String username, String password) {
//
//        // authenticationToken 객체를 통해 Authentication 객체 생성
//        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//
//        // 인증 정보를 기준으로 jwt access 토큰 생성
//        String accessToken = tokenProvider.createJwt(authentication);
//
//        // 인증 정보를 기준으로 refresh token생성
//        Long tokenWeight = 1L;//((SocialUser)authentication.getPrincipal()).getTokenWeight();
//        String refreshToken = refreshTokenProvider.createRefreshToken(authentication, tokenWeight);
//
//        return ResponseToken.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    @Override
//    public ResponseToken refreshToken(String refreshToken) throws IllegalAccessException {
//        return null;
//    }
//
//    @Override
//    public void invalidateRefreshTokenByUsername(String username) {
//
//    }
//}
