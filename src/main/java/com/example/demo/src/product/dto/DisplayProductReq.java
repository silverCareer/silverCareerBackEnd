package com.example.demo.src.product.dto;

import lombok.Builder;

@Builder
public record DisplayProductReq(
        String category
) { }
