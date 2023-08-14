package com.example.demo.src.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class AuthAdapter extends User {
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
