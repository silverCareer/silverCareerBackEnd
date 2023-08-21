package com.example.demo.src.likes.service;

import com.example.demo.global.DistributeLock;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.CustomException;
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
    public ResponseEntity<CommonResponse> addLikesCount(final Long productIdx, final String username) {
        Product product = productRepository.findById(productIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        if (likeRepository.existsByProductIdxAndMemberEmail(productIdx, username)) {
            throw new CustomException(ErrorCode.EXIST_LIKES);
        }

        product.increaseLikesCount();
        Like like = Like.builder()
                .productIdx(product.getProductIdx())
                .memberEmail(username)
                .build();
        likeRepository.save(like);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("좋아요 등록 성공")
                .build());
    }

    @Override
    @DistributeLock(key = "product: lock:{#productIdx}:{#username}")
    public ResponseEntity<CommonResponse> removeLikesCount(final Long productIdx, final String username) {
        Product product = productRepository.findById(productIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        product.decreaseLikesCount();
        Like like = likeRepository.findByProductIdxAndMemberEmail(productIdx, username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

        likeRepository.delete(like);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("좋아요 취소 성공")
                .build());
    }
}
