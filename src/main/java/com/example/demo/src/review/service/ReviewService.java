package com.example.demo.src.review.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.review.dto.RequestCreateReview;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity<CommonResponse> createReview(final String username, final Long productIdx, final RequestCreateReview requestCreateReview);
}
