package com.example.demo.src.likes.core;

import com.example.demo.src.likes.service.LikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class LikeRedisson {
    private static final int WAIT_TIME = 1;
    private static final int LEASE_TIME = 3;

    private final RedissonClient redissonClient;
    private final LikeServiceImpl likeServiceImpl;

    private void performWithLocking(Long productIdx, String username, LikeOperation likeOperation) {
        RLock rLock = redissonClient.getLock("상품ID: " + productIdx.toString());
        try {
            boolean available = rLock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS); // 1초로 대기, 최대 3초
            if (!available) {
                throw new IllegalAccessException("잠금 락 획득 실패!");
            }
            likeOperation.perform(productIdx, username);
        } catch (InterruptedException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } finally {
            rLock.unlock();
        }
    }

    public void addLike(Long productIdx, String username) {
        performWithLocking(productIdx, username, likeServiceImpl::addLikesCount);
    }

    public void removeLike(Long productIdx, String username) {
        performWithLocking(productIdx, username, likeServiceImpl::removeLikesCount);
    }
}
