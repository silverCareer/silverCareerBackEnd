package com.example.demo.global.security;


import com.example.demo.src.member.domain.Authority;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


public class TokenProvider {
    protected final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    protected static final String AUTHORITIES_KEY = "auth";

    protected final String secret;
    protected final long tokenValidityInMilliseconds;

    protected Key key;

    public TokenProvider(String secret, long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;

        //시크릿 값을 decode해서 키 변수에 할당
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Spring Security에서 인증된 사용자 정보를 표현하는 데 사용되는 Authentication
    // 인증이 성공한 사용자에 대한 정보를 제공한다.
    // 스프링 시큐리티에서 인증 및 인가를 처리하는데 사용한다.
    public String createJwt(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String reissueJwt(final String memberEmail, final String authority){
        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(memberEmail)
                .claim(AUTHORITIES_KEY, authority)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        //클레임 정보 추출
        // setSigningkey로 jwt 서명 검증하는데 사용되는 비밀키 설정
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 디비를 거치지 않고 토큰에서 값을 꺼내 바로 시큐리티 유저 객체를 만들어 Authentication을 만들어 반환하기에 유저네임, 권한 외 정보는 알 수 없게 한다.
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public JwtCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return JwtCode.ACCESS;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
            return JwtCode.DENIED;
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
            return JwtCode.EXPIRED;
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
            return JwtCode.DENIED;
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
            return JwtCode.DENIED;
        }
    }

    public Date extractExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 토큰 서명 검증을 위한 키
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

}
