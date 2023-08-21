package com.example.demo.src.likes.service;

import com.example.demo.global.DistributeLock;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.global.exception.error.likes.ExistLikesException;
import com.example.demo.global.exception.error.likes.NotFoundLikesException;
import com.example.demo.global.exception.error.product.NotFoundProductException;
import com.example.demo.src.likes.domain.Like;
import com.example.demo.src.likes.repository.LikeRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.chain.Command;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;

    @Override
    @DistributeLock(key = "product: lock:{#productIdx}:{#username}")
    public void addLikesCount(final Long productIdx, final String username) {
        Product product = productRepository.findById(productIdx).orElseThrow(NotFoundProductException::new);
        if (likeRepository.existsByProductIdxAndMemberEmail(productIdx, username)) throw new ExistLikesException();
        product.increaseLikesCount();
        Like like = Like.builder()
                .productIdx(product.getProductIdx())
                .memberEmail(username)
                .build();
        likeRepository.save(like);
    }

    @Override
    @DistributeLock(key = "product: lock:{#productIdx}:{#username}")
    public void removeLikesCount(final Long productIdx, final String username) {
        Product product = productRepository.findById(productIdx).orElseThrow(NotFoundProductException::new);
        Like like = likeRepository.findByProductIdxAndMemberEmail(productIdx, username).orElseThrow(NotFoundLikesException::new);
        product.decreaseLikesCount();
        likeRepository.delete(like);
    }
}
