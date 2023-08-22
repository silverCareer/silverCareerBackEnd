package com.example.demo.src.likes.service;

import com.example.demo.src.likes.core.LikeRedisson;
import com.example.demo.src.likes.repository.LikeRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeRedissonTest {
    @InjectMocks
    private LikeRedisson likeRedisson;
    @Mock
    private LikeServiceImpl likeServiceImpl;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private LikeRepository likeRepository;

    @Test
    @DisplayName("좋아요 동시성 테스트(성공 케이스)")
    void addLikeRedisson() throws InterruptedException {
        // Mock객체로 RLock 객체 생성 및 동작 설정
        RLock mockLock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(mockLock);
        when(mockLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        int threads = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threads); //스레드 풀 50개 생성
        CountDownLatch latch = new CountDownLatch(threads); // 모든 스레드의 작업이 완료될 때까지 대기하는 동기화 메커니즘 생성
        AtomicInteger countCall = new AtomicInteger(0); // 메서드 호출 횟수를 추적하는 AtomicInteger 생성

        ArgumentCaptor<Long> productIdxCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);

        // likeServiceImpl의 addLikesCount 메서드 호출 시 호출 횟수 증가 및 결과값 반환하는 Mock
        doAnswer(invocation -> {
            countCall.incrementAndGet();
            return null;
        }).when(likeServiceImpl).addLikesCount(productIdxCaptor.capture(), usernameCaptor.capture());

        for (int i = 0; i < threads; i++) {
            String username = "kwak" + i;
            executorService.submit(() -> {
                try {
                    // 테스트 대상 메서드 호출 (동시성 테스트)
                    likeRedisson.addLike(1L, username);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        /**
         * 스레드 수 50개
         * 락 대기 시간 1초
         * 락 유지 시간 3초
         *
         * 총 좋아요 요청 수 50번
         * 요청 된 메서드 카운트 50번  -> 일치
         *
         */

        verify(likeServiceImpl, times(threads)).addLikesCount(eq(1L), anyString());

        List<Long> capturedProductIdxValues = productIdxCaptor.getAllValues();
        List<String> capturedUsernameValues = usernameCaptor.getAllValues();

        for (int i = 0; i < capturedProductIdxValues.size(); i++) {
            Long productIdx = capturedProductIdxValues.get(i);
            String username = capturedUsernameValues.get(i);
        }

        Assertions.assertEquals(threads, countCall.get());
        Assertions.assertEquals(threads, capturedProductIdxValues.size());
        Assertions.assertEquals(threads, capturedUsernameValues.size());
    }
    @Test
    @DisplayName("좋아요 취소 동시성 테스트 (성공 케이스)")
    void removeLikeRedisson() throws InterruptedException {
        RLock mockLock = mock(RLock.class);
        when(redissonClient.getLock(anyString())).thenReturn(mockLock);
        when(mockLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        int threads = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        AtomicInteger countCall = new AtomicInteger(0);

        ArgumentCaptor<Long> productIdxCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);

        doAnswer(invocation -> {
            countCall.incrementAndGet();
            return null;
        }).when(likeServiceImpl).removeLikesCount(productIdxCaptor.capture(), usernameCaptor.capture());

        for (int i = 0; i < threads; i++) {
            String username = "kwak" + i;
            executorService.submit(() -> {
                try {
                    likeRedisson.removeLike(1L, username);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        /**
         * 스레드 수: 50
         * 락 대기 시간: 1초
         * 락 보유 시간: 3초
         *
         * 총 10번의 좋아요 취소 요청
         * 요청된 메서드 호출 횟수: 50번 -> 일치
         *
         */
        verify(likeServiceImpl, times(threads)).removeLikesCount(eq(1L), anyString());

        List<Long> capturedProductIdxValues = productIdxCaptor.getAllValues();
        List<String> capturedUsernameValues = usernameCaptor.getAllValues();

        for (int i = 0; i < capturedProductIdxValues.size(); i++) {
            Long productIdx = capturedProductIdxValues.get(i);
            String username = capturedUsernameValues.get(i);
        }

        Assertions.assertEquals(threads, countCall.get());
        Assertions.assertEquals(threads, capturedProductIdxValues.size());
        Assertions.assertEquals(threads, capturedUsernameValues.size());
    }
}
