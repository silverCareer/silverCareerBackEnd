package com.example.demo.src.account.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestAccountCharge(
        @NonNull
        String bankName,
        @NonNull
        String accountNum,
        @NonNull
        Long balance,
        @NonNull
        Long memberIdx
) {
}