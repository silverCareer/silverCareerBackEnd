package com.example.demo.src.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestLogin(
        @NotNull
        @Size(min = 3, max = 50)
        String username,
        @NotNull
        @Size(min = 5, max = 100)
        String password,
        String email
) {


}
