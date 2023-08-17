package com.example.demo.src.likes.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.likes.core.LikeRedisson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/detail/{productId}/likes")
public class LikeController {
    private final LikeRedisson likeRedisson;

    @PostMapping
    public ResponseEntity<CommonResponse> addLikesCount(@AuthenticationPrincipal(expression = "username") String username,
                                                        @Valid @PathVariable Long productId) {
        likeRedisson.addLike(username, productId);
        return ResponseEntity.ok(CommonResponse.builder()
                .success(true)
                .response("좋아요 등록 성공")
                .build());
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse> removeLikesCount(@AuthenticationPrincipal(expression = "username") String username,
                                                           @Valid @PathVariable Long productId) {
        likeRedisson.removeLike(username, productId);
        return ResponseEntity.ok(CommonResponse.builder()
                .success(true)
                .response("좋아요 취소 성공")
                .build());
    }
}
