package com.example.demo.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class RefreshTokenProvider extends TokenProvider {
    private static final String WEIGHT = "token-weight";

    public RefreshTokenProvider(String secret, long tokenValidityInSeconds) {
        super(secret, tokenValidityInSeconds);
    }

    public String createRefreshToken(Authentication authentication, Long tokenWeight) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + super.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(WEIGHT, tokenWeight)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public long getTokenWeight(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(String.valueOf(claims.get(WEIGHT)));
    }
}
