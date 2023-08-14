package com.example.demo.src.review.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.review.domain.Review;
import com.example.demo.src.review.dto.CreateReviewReq;
import com.example.demo.src.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    final ReviewRepository reviewRepository;
    final MemberRepository memberRepository;
    final ProductRepository productRepository;

    @Override
    @Transactional
    public void createReview(String username, Long productIdx, CreateReviewReq createReviewReq){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Product product = productRepository.findProductByProductIdx(productIdx)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        String postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Review review = Review.builder()
                .context(createReviewReq.getContext())
                .postDate(postDate)
                .rating(createReviewReq.getRating())
                .member(member)
                .product(product)
                .build();
        reviewRepository.save(review);
        product.addReview(review);

    }
}
