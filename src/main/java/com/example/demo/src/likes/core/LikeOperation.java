package com.example.demo.src.likes.core;

@FunctionalInterface
public interface LikeOperation {
    void perform(String username, Long productIdx);
}
