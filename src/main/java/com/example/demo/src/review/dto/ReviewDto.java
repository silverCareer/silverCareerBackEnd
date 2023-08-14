package com.example.demo.src.review.dto;


import com.example.demo.src.review.domain.Review;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDto {
    private Long reviewIdx;
    private String authorName;
    private String reviewContext;
    private Long rating;
    private String postDate;
    public static ReviewDto of(Review review){
        return ReviewDto.builder()
                .reviewIdx(review.getReviewIdx())
                .authorName(review.getMember().getName())
                .reviewContext(review.getContext())
                .rating(review.getRating())
                .postDate(review.getPostDate())
                .build();
    }
}
