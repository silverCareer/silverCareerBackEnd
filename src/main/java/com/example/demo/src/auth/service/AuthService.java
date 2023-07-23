package com.example.demo.src.auth.service;

import com.example.demo.global.security.RefreshTokenProvider;
import com.example.demo.global.security.TokenProvider;
import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.domain.AuthAdapter;
import com.example.demo.src.auth.dto.ResponseToken;
import com.example.demo.src.auth.repository.AuthRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthRepository authRepository;

//    private final AuthDao authDao;
//    private final AuthProvider authProvider;

    public AuthService(TokenProvider tokenProvider, RefreshTokenProvider refreshTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, AuthRepository authRepository) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authRepository = authRepository;
    }

    public ResponseToken authenticate(String username, String password) {
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createJwt(authentication);

        Long tokenWeight = ((AuthAdapter)authentication.getPrincipal()).getAuth().getTokenWeight();
        String refreshToken = refreshTokenProvider.createRefreshToken(authentication, tokenWeight);
        return ResponseToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseToken refreshToken(String refreshToken) throws IllegalAccessException {
        // 먼저 리프레시 토큰을 검증한다.
        if(!refreshTokenProvider.validateToken(refreshToken)) throw new IllegalAccessException("리프레시 토큰이 유효하지 않습니다!");

        // 리프레시 토큰 값을 이용해 사용자를 꺼낸다.
        // refreshTokenProvider과 TokenProvider는 다른 서명키를 가지고 있기에 refreshTokenProvider를 써야함
        Authentication authentication = refreshTokenProvider.getAuthentication(refreshToken);
        Auth auth = authRepository.findOneWithAuthoritiesByUsername(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException(authentication.getName() + "을 찾을 수 없습니다"));
        // 사용자 디비 값에 있는 것과 가중치 비교, 디비 가중치가 더 크다면 유효하지 않음
        if(auth.getTokenWeight() > refreshTokenProvider.getTokenWeight(refreshToken)) throw new IllegalAccessException("리프레시 토큰이 유효하지 않습니다!");

        // 리프레시 토큰에 담긴 값을 그대로 액세스 토큰 생성에 활용한다.
        String accessToken = tokenProvider.createJwt(authentication);
        // 기존 리프레시 토큰과 새로 만든 액세스 토큰을 반환한다.
        return ResponseToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Account 가중치를 1 올림으로써 해당 username 리프레시토큰 무효화
    @Transactional
    public void invalidateRefreshTokenByUsername(String username) {
        Auth auth = authRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "-> 찾을 수 없습니다"));
        auth.increaseTokenWeight(); // 더티체킹에 의해 엔티티 변화 반영
    }

//    private final JwtService jwtService;

//    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException {
//        String pwd;
//        try {
//            pwd = new SHA256().encrypt(postLoginReq.getPassword());
//        } catch (Exception ignored) {
//            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
//        }
//        postLoginReq.setPassword(pwd);
//
//        PostLoginRes postLoginRes = authProvider.findByEmailAndPassword(postLoginReq);
//
//        String jwt = jwtService.createJwt(postLoginRes.getMemberIdx());
//
//        return new PostLoginRes(jwt, postLoginRes.getMemberIdx());
//    }
}