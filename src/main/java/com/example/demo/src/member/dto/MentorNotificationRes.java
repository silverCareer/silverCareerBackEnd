package com.example.demo.src.member.dto;

import com.example.demo.src.suggestion.domain.Suggestion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentorNotificationRes {
    private Long suggestionIdx;
    private String suggestionTitle;
    private String suggester;
    private Boolean suggestionStatus;

    public static MentorNotificationRes of(Suggestion suggestion, Boolean suggestionStatus){
        return MentorNotificationRes.builder()
                .suggestionIdx(suggestion.getSuggestionIdx())
                .suggestionTitle(suggestion.getTitle())
                .suggester(suggestion.getMember().getName())
                .suggestionStatus(suggestionStatus)
                .build();
    }
}
