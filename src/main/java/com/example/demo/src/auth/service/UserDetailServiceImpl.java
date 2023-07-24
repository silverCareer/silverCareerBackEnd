package com.example.demo.src.auth.service;

import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.domain.AuthAdapter;
import com.example.demo.src.auth.repository.AuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final AuthRepository authRepository;

    public UserDetailServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    @Transactional //loadUserByUsername 재정의
    public UserDetails loadUserByUsername(final String username) {
        Auth auth = authRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException(username+"-> db에서 찾을 수 없음"));
        if(!auth.isActivated())
            throw new RuntimeException(auth.getUsername()+"-> 비활성 계정입니다.");
        return new AuthAdapter(auth);
    }
}
