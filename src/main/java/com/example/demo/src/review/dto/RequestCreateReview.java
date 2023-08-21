package com.example.demo.src.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreateReview {
    private Long productIdx;
    private String context;
    private Long rating;
}
