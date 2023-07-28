package com.example.demo.src.member.dto;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestMemberRegister(
        @NotNull
        String username,
        @NotNull
        String password,
        @NotNull
        String email,
        @NotNull
        String phoneNumber,
        @NotNull
        Long age,
        String provider

) {
}
