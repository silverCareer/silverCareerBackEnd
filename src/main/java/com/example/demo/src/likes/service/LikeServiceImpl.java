package com.example.demo.src.likes.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.likes.domain.Like;
import com.example.demo.src.likes.repository.LikeRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;

    @Override
    public void addLikesCount(final String username, final Long productIdx) {
        Product product = productRepository.findById(productIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        if (likeRepository.existsByProductIdxAndMemberEmail(username, productIdx)) {
            throw new CustomException(ErrorCode.EXIST_LIKES);
        }

        Like like = Like.builder()
                .productIdx(product.getProductIdx())
                .memberEmail(username)
                .build();
        product.increaseLikesCount();
        likeRepository.save(like);
    }

    @Override
    public void removeLikesCount(final String username, final Long productIdx) {
        Product product = productRepository.findById(productIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Like like = likeRepository.findByProductIdxAndMemberEmail(username, productIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

        product.decreaseLikesCount();
        likeRepository.delete(like);
    }
}
