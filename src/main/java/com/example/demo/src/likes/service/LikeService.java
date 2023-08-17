package com.example.demo.src.likes.service;

public interface LikeService {
    void addLikesCount(final String username, final Long productIdx);
    void removeLikesCount(final String username, final Long productIdx);
}
