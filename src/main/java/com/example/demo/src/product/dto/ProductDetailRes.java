package com.example.demo.src.product.dto;


import lombok.Builder;

@Builder
public record ProductDetailRes (
        Long productIdx,
        String productName,
        String category,
        String description,
        Long price,
        String image,
        Long likes

) { }
