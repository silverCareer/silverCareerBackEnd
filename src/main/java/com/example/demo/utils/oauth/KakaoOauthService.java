package com.example.demo.utils.oauth;

import com.example.demo.src.kakao.dto.KakaoOauth;
import com.example.demo.src.kakao.dto.KakaoUser;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoOauthService implements OauthService {

    //@Value("${kakao-open-api.auth.rest-api-key}")
    private String restApiKey = "3fc7941e2346eb6d089f1032ffe4c40b";
    private final PasswordEncoder passwordEncoder;

    public KakaoOauth getUserToken(String code) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", restApiKey);
        params.add("redirect_uri", "http://localhost:9000/api/kakao");
        //params.add("redirect_uri", "https://www.silvercareer.shop/api/kakao");
        params.add("code", code);
        params.add("scope", "account_email");
        params.add("scope", "profile_image");
        params.add("scope", "gender");
        params.add("scope", "age_ragne");
        params.add("scope", "birthday");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON -> 액세스 토큰 파싱
        String tokenJson = response.getBody();
        JSONObject rjson = new JSONObject(tokenJson);

        KakaoOauth oauthInfo = KakaoOauth.builder()
                .accessToken(rjson.getString("access_token"))
                .refreshToken(rjson.getString("refresh_token"))
                .refreshValidTime(rjson.getInt("refresh_token_expires_in"))
                .build();

        return oauthInfo;
    }

    public KakaoUser getUserInfo(String accessToken) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());

        //비밀번호 생성 이메일+생일
        String email = body.getJSONObject("kakao_account").getString("email");
        String birth = body.getJSONObject("kakao_account").getString("birthday");

        String ageRange = body.getJSONObject("kakao_account").getString("age_range");

        /*Auth userInfo = Auth.builder()
                .authIdx(body.getLong("id"))
                .username("백연정")
                .userImage(body.getJSONObject("properties").getString("profile_image"))
                .email(email)
                .phoneNumber("01051499261")
                .age(29L)
                .password(passwordEncoder.encode(email + birth))
                .provider("kakao")
                .activated(true)
                .build();
         */

        /* authorities는 Auth에서 */
        KakaoUser userInfo = KakaoUser.builder()
                .userIdx(body.getLong("id"))
                .userEmail(email)
                .userImage(body.getJSONObject("properties").getString("profile_image"))
                .password(passwordEncoder.encode(email + birth))
                .provider("kakao")
                .activated(true)
                .build();

        return userInfo;
    }
}
