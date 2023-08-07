package com.example.demo.src.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestSingUp {
    @NotNull
    private String email;
    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Long age;

    private String authority;
    private String category;
    private String bankName;
    private String accountNum;

}
