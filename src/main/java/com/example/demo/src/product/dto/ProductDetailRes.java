package com.example.demo.src.product.dto;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.review.domain.Review;
import com.example.demo.src.review.dto.ReviewDto;
import lombok.*;

import java.util.List;

@Data
@Builder
public class ProductDetailRes {
    private Long productIdx;
    private String productName;
    private String category;
    private String address;
    private String description;
    private Long price;
    private String image;
    private Long likes;
    private String memberName;
    private String memberCareer;
    private List<ReviewDto> reviews;
}
