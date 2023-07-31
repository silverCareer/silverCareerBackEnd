package com.example.demo.utils.oauth;

import com.example.demo.src.kakao.dto.KakaoOauth;
import com.example.demo.src.kakao.dto.KakaoUser;

public interface OauthService {

    KakaoOauth getUserToken(String code);
    KakaoUser getUserInfo(String accessToken);
}
