package com.example.demo.src.auth.dto;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestRefresh(
        @NotNull
        String token
) {

}
