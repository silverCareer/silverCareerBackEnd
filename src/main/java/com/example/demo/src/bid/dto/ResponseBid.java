package com.example.demo.src.bid.dto;

import com.example.demo.src.bid.domain.Bid;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseBid {
    private Long bidIdx;
    private String title;
    private String category;
    private Long price;
    private String mentorName;
    private String status;

    public static ResponseBid of(Bid bid) {
        return ResponseBid.builder()
                .bidIdx(bid.getBidIdx())
                .title(bid.getSuggestion().getTitle())
                .category(bid.getSuggestion().getCategory())
                .price(bid.getPrice())
                .mentorName(bid.getMember().getName())
                .status(bid.getStatus().toString())
                .build();
    }
}
