package com.example.demo.src.likes.service;

import com.example.demo.global.exception.dto.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface LikeService {
    void addLikesCount(final Long productIdx, final String username);
    void removeLikesCount(final Long productIdx, final String username);
}
