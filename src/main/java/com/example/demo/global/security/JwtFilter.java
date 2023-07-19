package com.example.demo.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;


import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    //로깅을 통해 로그를 관리한다. 디버깅 정보, 오류 메세지 등을 기록
    //JWTFilter가 어떤 요청을 처리했는지, 토큰이 유효한지 여부, 인증 과정 등의 정보를 로그로 남길 수 있습니다.
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;
    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    // 실제 필터링 로직은 doFilter 안에 들어가게 된다. GenericFilterBean을 받아 구현하고
    // dofilter는 토큰의 인증정보를 SecurityContext 안에 저장하는 역할 수행
    // 현재는 jwtFilter 통과 시 loadUserByUsername을 호출하여 디비를 거치지 않으므로
    // 시큐리티 컨텍스트에는 엔티티 정보를 온전히 가지지 않는다.
    // 즉 loadUserByUsername을 호출하는 인증 API를 제외하고는 유저네임, 권한만 가지고 있으므로
    // Account 정보가 필요하다면 디비에서 꺼내와야한다.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
