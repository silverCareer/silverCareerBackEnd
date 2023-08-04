package com.example.demo.src.member.service;

import com.example.demo.src.member.domain.AuthAdapter;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        Member member = memberRepository.findOneWithAuthorityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "-> db에 맞는 값 찾을 수 없음"));

        if (!member.isActivated()) throw new RuntimeException(member.getUsername());
        return new AuthAdapter(member);

    }
}

