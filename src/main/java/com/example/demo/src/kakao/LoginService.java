package com.example.demo.src.kakao;

import com.example.demo.src.auth.domain.SocialUser;
import com.example.demo.src.kakao.dto.KakaoOauth;
import com.example.demo.utils.oauth.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final KakaoOauthService kakaoOauthService;

    public SocialUser kakaoLogin(String code) {

        //1. 사용자 token 가져오기 (accessToken, refreshToken, expiredTime)
        KakaoOauth oauth = kakaoOauthService.getUserToken(code);

        //2. token을 통해 사용자 정보 가져오기
        SocialUser userInfo = kakaoOauthService.getUserInfo(oauth.getAccessToken());

        //3. 사용자 회원가입 (가입여부 확인)
        //userInfo = kakaoCreateService.createKakaoUser(userInfo);

        return userInfo;
    }
}
