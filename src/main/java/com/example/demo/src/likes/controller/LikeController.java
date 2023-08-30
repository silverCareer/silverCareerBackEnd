package com.example.demo.src.likes.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.likes.core.LikeRedisson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/detail/{productIdx}/likes")
public class LikeController {
    private final LikeRedisson likeRedisson;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MENTOR','ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> addLikesCount(@AuthenticationPrincipal(expression = "username") String username,
                                                        @Valid @PathVariable Long productIdx) {
        return likeRedisson.addLike(productIdx, username);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MENTOR','ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> removeLikesCount(@AuthenticationPrincipal(expression = "username") String username,
                                                           @Valid @PathVariable Long productIdx) {
        return likeRedisson.removeLike(productIdx, username);
    }
}
