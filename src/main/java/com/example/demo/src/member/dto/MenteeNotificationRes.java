package com.example.demo.src.member.dto;

import com.example.demo.src.bid.domain.Bid;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenteeNotificationRes {
    private Long bidIdx;
    private Long price;
    private String mentorName;
    private String suggestionTitle;

    public static MenteeNotificationRes of(Bid bid){
        return MenteeNotificationRes.builder()
                .bidIdx(bid.getBidIdx())
                .price(bid.getPrice())
                .mentorName(bid.getMember().getName())
                .suggestionTitle(bid.getSuggestion().getTitle())
                .build();
    }

}
