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
    private String memberName;

    public static ResponseSuggestion of(Suggestion suggestion) {
        return ResponseSuggestion.builder()
                .suggestionIdx(suggestion.getSuggestionIdx())
                .category(suggestion.getCategory())
                .memberName(suggestion.getMember().getName())
                .build();
    }
}
