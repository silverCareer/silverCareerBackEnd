package com.example.demo.src.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class AuthAdapter extends User {
//    private Auth auth;
//
//    public AuthAdapter(Auth auth) {
//        super(auth.getUsername(), auth.getPassword(), authorities(auth.getAuthorities()));
//        this.auth = auth;
//    }
//
//    public Auth getAuth() {
//        return this.auth;
//    }
//    private static List<GrantedAuthority> authorities(Set<Authority> authorities) {
//        return authorities.stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
//                .collect(Collectors.toList());
//    }

    private Member member;

    public AuthAdapter(Member member){
        super(member.getUsername(), member.getPassword(), authority(member.getAuthority()));
        this.member = member;
    }

    public Member getAuth(){
        return this.member;
    }

    private static List<GrantedAuthority> authority(Authority auth){
        List<GrantedAuthority> tmp = new ArrayList<>();
        tmp.add(new SimpleGrantedAuthority(auth.getAuthorityName()));
        return tmp;
    }
}
