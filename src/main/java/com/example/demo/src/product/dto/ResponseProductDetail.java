package com.example.demo.src.product.dto;

import com.example.demo.src.review.dto.ReviewDto;
import lombok.*;

import java.util.List;

@Data
@Builder
public class ResponseProductDetail {
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
    private int status;
    private boolean isLiked;
    private List<ReviewDto> reviews;
}
