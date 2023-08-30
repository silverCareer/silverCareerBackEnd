package com.example.demo.src.member.dto;

import com.example.demo.src.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMyInfo {
    private String email; // 이메일
    private String userImage; // 유저 프로필 이미지
    private String name; // 이름
    private String career; // 멘토일 경우 커리어
    private Long balance; // 멘티일 경우 잔액
    private String authority;
    private Long age;
    private String phoneNumber;

    public static ResponseMyInfo of(Member member) {
        if (member == null) return null;
        return ResponseMyInfo.builder()
                .email(member.getUsername())
                .userImage(member.getUserImage())
                .name(member.getName())
                .career(member.getCareer())
                .balance(member.getBalance())
                .authority(member.getAuthority().getAuthorityName())
                .age(member.getAge())
                .phoneNumber(member.getPhoneNumber())
                .build();
    }
}
