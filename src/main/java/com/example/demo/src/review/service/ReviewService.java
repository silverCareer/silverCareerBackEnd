package com.example.demo.src.review.service;

import com.example.demo.src.review.dto.CreateReviewReq;

public interface ReviewService {
    void createReview(String username, Long productIdx, CreateReviewReq createReviewReq);
}
