package com.example.demo.src.review.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.global.exception.error.member.NotFoundMemberException;
import com.example.demo.global.exception.error.product.NotFoundProductException;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.review.domain.Review;
import com.example.demo.src.review.dto.RequestCreateReview;
import com.example.demo.src.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse> createReview(final String username, final Long productIdx, final RequestCreateReview requestCreateReview){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(NotFoundMemberException::new);
        Product product = productRepository.findProductByProductIdx(productIdx)
                .orElseThrow(NotFoundProductException::new);

        String postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Review review = Review.builder()
                .context(requestCreateReview.getContext())
                .postDate(postDate)
                .rating(requestCreateReview.getRating())
                .member(member)
                .product(product)
                .build();

        reviewRepository.save(review);
        product.addReview(review);

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("댓글 등록 성공").build());
    }
}
