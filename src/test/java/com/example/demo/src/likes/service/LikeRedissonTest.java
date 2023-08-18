package com.example.demo.src.likes.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.likes.core.LikeRedisson;
import com.example.demo.src.likes.domain.Like;
import com.example.demo.src.likes.repository.LikeRepository;
import com.example.demo.src.member.domain.Authority;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LikeRedissonTest {

    @Autowired
    private LikeRedisson likeRedisson;
    @Autowired
    private LikeServiceImpl likeServiceImpl;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
//        Authority authority = Authority.builder()
//                .authorityName("ROLE_MENTOR")
//                .build();
//
//        Member member = Member.builder()
//                .email("kdh3213@gmail.com")
//                .password("sungbaksa1@")
//                .name("알빠냐")
//                .phoneNumber("0101232141241")
//                .age(97L)
//                .category("전투직")
//                .authority(authority)
//                .activated(true)
//                .build();
//        memberRepository.save(member);
//
//        Product product = Product.builder()
//                .productIdx(1L)
//                .productName("주먹 자랑 교실")
//                .category("기술직")
//                .address("평안도 개경시")
//                .description("덤벼")
//                .price(60000000000000000L)
//                .image("fasdgasggqrqr")
//                .saleCount(10L)
//                .likes(0L)
//                .member(member)
//                .build();
//        productRepository.save(product);
//
//        Like like = Like.builder()
//                .productIdx(1L)
//                .memberEmail("kdh3213@gmail.com")
//                .build();
//        likeRepository.save(like);
    }
    @AfterEach
    void after() {
        likeRepository.deleteAll();
//        productRepository.deleteAll();
//        memberRepository.deleteAll();
    }

    @Test
    void testAddLikeWithConcurrency() throws InterruptedException {
        int numThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            String username = "kdh3213@gmail.com" + i;
            executorService.submit(() -> {
                try {
                    likeRedisson.addLike(30L, username);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Like like = likeRepository.findById(30L).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        System.out.println(like.getMemberEmail());
        Assertions.assertEquals(0L, like.getLikesIdx());
    }
}
