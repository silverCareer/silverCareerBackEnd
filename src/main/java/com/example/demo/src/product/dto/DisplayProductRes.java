package com.example.demo.src.product.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DisplayProductRes(
        List<ProductDto> products
) {
    @Builder
    public record ProductDto(
            Long productIdx,
            String productName,
            String productDescription,
            String productImage,
            Long productLikes,
            Long productPrice
    ){ }
}
