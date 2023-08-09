package com.example.demo.src.suggestion.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record RequestCreateSuggestion(
        @NotNull
        String description,
        @NotNull
        String category,
        @NotNull
        Long price
) {
}
