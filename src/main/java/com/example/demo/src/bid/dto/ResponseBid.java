package com.example.demo.src.bid.dto;

import com.example.demo.src.bid.domain.Bid;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseBid {
    private Long price;
    private String memberName;

    public static ResponseBid of(Bid bid) {
        return ResponseBid.builder()
                .price(bid.getPrice())
                .memberName(bid.getMember().getName())
                .build();
    }
}
