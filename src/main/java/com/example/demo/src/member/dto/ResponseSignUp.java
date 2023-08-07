package com.example.demo.src.member.dto;

import com.example.demo.src.member.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSignUp {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private Long age;
    private Authority authority;

}
