package com.example.demo.src.member.dto;

import lombok.Data;

@Data
public class MemberPhonePatchDto {
    private String phoneNum;

    public MemberPhonePatchDto() {
    }

    public MemberPhonePatchDto(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
