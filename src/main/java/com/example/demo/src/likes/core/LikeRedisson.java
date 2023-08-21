package com.example.demo.src.likes.core;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.likes.service.LikeService;
import com.example.demo.src.likes.service.LikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class LikeRedisson {
    private static final int WAIT_TIME = 1;
    private static final int LEASE_TIME = 3;
    private final RedissonClient redissonClient;
    private final LikeServiceImpl likeServiceImpl;
    private final LikeService likeService;

    private ResponseEntity<CommonResponse> performWithLocking(Long productIdx, String username, LikeOperation likeOperation,String responseMessage) {
        RLock rLock = redissonClient.getLock("상품 ID: " + productIdx.toString() + "멤버: " + username);
        try {
            boolean available = rLock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!available) {
                throw new IllegalAccessException("락 획득 실패!");
            }
            likeOperation.perform(productIdx, username);
            return ResponseEntity.ok(CommonResponse.builder()
                    .success(true)
                    .response(responseMessage)
                    .build());
        } catch (InterruptedException | IllegalAccessException ex) {
            throw new RuntimeException(ex);

        } finally {
            if (rLock != null && rLock.isLocked()) {
                rLock.unlock();
            }
        }
    }

    public ResponseEntity<CommonResponse> addLike(Long productIdx, String username) {
        return performWithLocking(productIdx, username, likeService::addLikesCount, "좋아요 등록 성공.");

    }

    public ResponseEntity<CommonResponse> removeLike(Long productIdx, String username) {
        return performWithLocking(productIdx, username, likeService::removeLikesCount, "좋아요 취소 성공.");
    }
}
