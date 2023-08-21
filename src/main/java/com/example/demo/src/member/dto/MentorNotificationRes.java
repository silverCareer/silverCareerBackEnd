package com.example.demo.src.member.dto;

import com.example.demo.src.suggestion.domain.Suggestion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentorNotificationRes {
    private Long suggestionIdx;
    private String title;
    private String category;
    private Long price;
    private String suggester;
    private Boolean suggestionStatus;

    public static MentorNotificationRes of(Suggestion suggestion, Boolean suggestionStatus){
        return MentorNotificationRes.builder()
                .suggestionIdx(suggestion.getSuggestionIdx())
                .title(suggestion.getTitle())
                .category(suggestion.getCategory())
                .price(suggestion.getPrice())
                .suggester(suggestion.getMember().getName())
                .suggestionStatus(suggestionStatus)
                .build();
    }
}
