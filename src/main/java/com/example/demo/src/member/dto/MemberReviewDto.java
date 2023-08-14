package com.example.demo.src.member.dto;

import com.example.demo.src.member.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberReviewDto {
    private String username;
    private String name;

    public static MemberReviewDto of(Member member){
        return MemberReviewDto.builder()
                .username(member.getUsername())
                .name(member.getName())
                .build();
    }
}
