package com.example.demo.src.member;

import com.example.demo.src.auth.domain.Auth;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record ResponseMemberRegister(
        String username,
        String password,
        String email,
        Long tokenWeight,
        Set<String> authoritySet

) {
    public static ResponseMemberRegister of(Auth auth) {
        if (auth == null) return null;
        return ResponseMemberRegister.builder()
                .username(auth.getUsername())
                .password(auth.getPassword())
                .email(auth.getEmail())
                .tokenWeight(auth.getTokenWeight())
                .authoritySet(auth.getAuthorities().stream()
                        .map(authority -> authority.getAuthorityName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
