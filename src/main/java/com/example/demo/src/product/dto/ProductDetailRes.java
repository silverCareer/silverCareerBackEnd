package com.example.demo.src.product.dto;

import com.example.demo.src.review.domain.Review;
import lombok.*;

@Builder
public class ProductDetailRes {
    private Long productIdx;
    private String productName;
    private String category;
    private String description;
    private Long price;
    private String image;
    private Long likes;
//        Review review

}
