package com.example.demo.src.review.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.review.dto.RequestCreateReview;
import com.example.demo.src.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create/{productIdx}")
    public ResponseEntity<CommonResponse> createReview(@AuthenticationPrincipal(expression = "username") String username,
                                                       @Valid @PathVariable Long productIdx,
                                                       @Valid @RequestBody RequestCreateReview requestCreateReview){
        return reviewService.createReview(username, productIdx, requestCreateReview);
    }
}
