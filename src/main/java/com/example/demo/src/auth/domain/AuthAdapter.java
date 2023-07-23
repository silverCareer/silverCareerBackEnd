package com.example.demo.src.auth.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthAdapter extends User {
    private Auth auth;

    public AuthAdapter(Auth auth) {
        super(auth.getUsername(), auth.getPassword(), authorities(auth.getAuthorities()));
        this.auth = auth;
    }

    public Auth getAuth() {
        return this.auth;
    }
    private static List<GrantedAuthority> authorities(Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
    }
}
