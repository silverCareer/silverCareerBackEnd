package com.example.demo.src.bid.dto;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
public class RequestBid {
    @NotNull
    private Long price;
}
