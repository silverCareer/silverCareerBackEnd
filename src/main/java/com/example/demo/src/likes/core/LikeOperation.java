package com.example.demo.src.likes.core;

@FunctionalInterface
public interface LikeOperation {
    void perform(Long productIdx, String username);
}
