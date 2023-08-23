package com.example.demo.src.suggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
