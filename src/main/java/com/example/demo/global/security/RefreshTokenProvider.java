package com.example.demo.global.security;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;


public class RefreshTokenProvider extends TokenProvider {
    private static final String WEIGHT = "token-weight";

    public RefreshTokenProvider(String secret, long tokenValidityInSeconds) {
        super(secret, tokenValidityInSeconds);
    }

    public String createRefreshToken(Long tokenWeight) {
        String id = UUID.randomUUID().toString();
        long now = (new Date()).getTime();
        Date validity = new Date(now + super.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(id)
                .claim(WEIGHT, tokenWeight)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();
    }

    public long getTokenWeight(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object weight = claims.get(WEIGHT);
        if (weight != null) {
            return Long.parseLong(String.valueOf(weight));
        } else {
            throw new RuntimeException("토큰 가중치를 찾을 수 없습니다.");
        }
    }
}
