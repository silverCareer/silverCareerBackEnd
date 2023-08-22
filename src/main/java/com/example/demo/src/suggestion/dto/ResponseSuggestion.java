package com.example.demo.src.suggestion.dto;

import com.example.demo.src.suggestion.domain.Suggestion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseSuggestion {
    private Long suggestionIdx;
    private String title;
    private String description;
    private String category;
    private Long price;
    private String suggester;

    public static ResponseSuggestion of(Suggestion suggestion) {
        return ResponseSuggestion.builder()
                .suggestionIdx(suggestion.getSuggestionIdx())
                .title(suggestion.getTitle())
                .description(suggestion.getDescription())
                .category(suggestion.getCategory())
                .price(suggestion.getPrice())
                .suggester(suggestion.getMember().getName())
                .build();
    }
}
