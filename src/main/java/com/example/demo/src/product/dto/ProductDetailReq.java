package com.example.demo.src.product.dto;

import lombok.Builder;

@Builder
public record ProductDetailReq (
        Long productIdx
){ }
