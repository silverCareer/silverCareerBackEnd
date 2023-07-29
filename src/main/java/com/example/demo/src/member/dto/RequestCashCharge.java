package com.example.demo.src.member.dto;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestCashCharge(
        @NotNull
        Long authIdx,
        @NotNull
        Long cash
) {
}
