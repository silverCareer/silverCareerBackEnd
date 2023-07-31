package com.example.demo.src.kakao.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SocialUserDto {
    private Long userId;
    private String userEmail;
    private String userName;
    private String userImage;
    private String userAgeRange; //type미정
    private String userBirth; //type미정
    
    @Builder
    public SocialUserDto(Long userId, String userEmail, String userImage,
                         String userAgeRange, String userBirth) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userAgeRange = userAgeRange;
        this.userBirth = userBirth;
    }
}
