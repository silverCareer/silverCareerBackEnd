//package com.example.demo.src.kakao;
//
//import com.example.demo.global.exception.BaseResponse;
//import com.example.demo.global.security.CustomJwtFilter;
//import com.example.demo.src.member.dto.ResponseToken;
//import com.example.demo.src.kakao.dto.KakaoUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final LoginService loginService;
//    private final KakaoMemberServiceImpl authService;
//
//    @GetMapping("/kakao")
//    public BaseResponse<ResponseLoginRegister> socialLogin (
//            @RequestParam String code
//    ) throws IllegalAccessException {
//
//        //카카오 로그인 (및 회원가입)
//        KakaoUser user = loginService.kakaoLogin(code);
//
//        //JWT 발급
//        ResponseToken token = authService.authenticate(user.userEmail(), user.password());
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.accessToken());
//
//        ResponseLoginRegister responseLoginRegister = ResponseLoginRegister.of(user, token);
//
//        return new BaseResponse<>(responseLoginRegister);
//    }
//
//}
