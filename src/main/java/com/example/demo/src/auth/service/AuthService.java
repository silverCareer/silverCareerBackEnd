package com.example.demo.src.auth.service;

import com.example.demo.global.security.RefreshTokenProvider;
import com.example.demo.global.security.TokenProvider;
import com.example.demo.src.auth.dto.ResponseToken;
import com.example.demo.src.auth.repository.AuthRepository;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

public interface AuthService {

    ResponseToken authenticate(String username, String password);
    ResponseToken refreshToken(String refreshToken) throws IllegalAccessException;
    void invalidateRefreshTokenByUsername(String username);
}
