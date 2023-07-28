package com.example.demo.utils.oauth;

import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.kakao.dto.KakaoOauth;

public interface OauthService {

    KakaoOauth getUserToken(String code);
    Auth getUserInfo(String accessToken);
}
