package com.example.demo.src.suggestion.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
public class RequestCreateSuggestion{
        @NotNull
        private String title;
        @NotNull
        private String description;
        @NotNull
        private String category;
        @NotNull
        private Long price;
}
