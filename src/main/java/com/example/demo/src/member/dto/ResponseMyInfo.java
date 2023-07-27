package com.example.demo.src.member.dto;

import com.example.demo.src.auth.domain.Auth;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record ResponseMyInfo(
        String username,
        String password,
        String email,
        Long age,
        String provider,
        String phoneNumber,
        Long tokenWeight,
        Set<String> authoritySet

) {
    public static ResponseMyInfo of(Auth auth) {
        if (auth == null) return null;
        return ResponseMyInfo.builder()
                .username(auth.getUsername())
                .password("통신보안ㅎ")
                .email(auth.getEmail())
                .phoneNumber(auth.getPhoneNumber())
                .age(auth.getAge())
                .provider(auth.getProvider())
                .tokenWeight(auth.getTokenWeight())
                .authoritySet(auth.getAuthorities().stream()
                        .map(authority -> authority.getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
