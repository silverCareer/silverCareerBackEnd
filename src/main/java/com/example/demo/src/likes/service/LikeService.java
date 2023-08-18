package com.example.demo.src.likes.service;

public interface LikeService {
    void addLikesCount(final Long productIdx, final String username);
    void removeLikesCount(final Long productIdx, final String username);
}
