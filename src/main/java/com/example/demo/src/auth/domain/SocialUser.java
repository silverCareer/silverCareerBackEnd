package com.example.demo.src.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SocialUser {
    //이게 table이 되려나?
    private Long userId;
    private String userEmail;
    private String userName;
    private String userImage;
    private String userAgeRange; //type미정
    private String userBirth; //type미정
    private String password;
   // private String tokenWeight; //tokenWeight for refreshToken

    @Builder
    public SocialUser(Long userId, String userEmail, String userName,
                      String userImage, String userAgeRange, String userBirth, String password) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userImage = userImage;
        this.userAgeRange = userAgeRange;
        this.userBirth = userBirth;
        this.password = password;
    }
}
