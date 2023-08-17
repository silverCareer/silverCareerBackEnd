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

    private void performWithLocking(String username, Long productIdx, LikeOperation likeOperation) {
        RLock rLock = redissonClient.getLock(productIdx.toString());
        try {
            boolean available = rLock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS); // 1초로 대기, 최대 3초
            if (!available) throw new IllegalAccessException("잠금 락 획득 실패!");

            likeOperation.perform(username, productIdx);
        } catch (InterruptedException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } finally {
            rLock.unlock();
        }
    }

    public void addLike(String username, Long productIdx){
        performWithLocking(username, productIdx, likeServiceImpl::addLikesCount);
    }
    public void removeLike(String username, Long productIdx){
        performWithLocking(username, productIdx, likeServiceImpl::removeLikesCount);
    }
}
