package com.example.demo.src.auth;

import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.security.CustomJwtFilter;
import com.example.demo.src.auth.dto.*;
import com.example.demo.src.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/authenticate") // Account 인증 API
    public ResponseEntity<ResponseToken> authorize(@Valid @RequestBody RequestLogin loginDto) {

        ResponseToken token = authService.authenticate(loginDto.username(), loginDto.password());

        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.accessToken());

        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
    }

    @PutMapping("/refresh")
    public ResponseEntity<ResponseToken> refreshToken(@Valid @RequestBody RequestRefresh refreshDto) throws IllegalAccessException {

        ResponseToken token = authService.refreshToken(refreshDto.token());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.accessToken());

        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);

    }


//        BaseException response = BaseException
//        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK)
//
//    private final AuthProvider authProvider;
//    private final AuthService authService;
//    private final JwtService jwtService;
//
//    @ResponseBody
//    @PostMapping("/login")
//    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq) {
//        try {
//
//            PostLoginRes postLoginRes = authService.login(postLoginReq);
//            return new BaseResponse<>(postLoginRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
}