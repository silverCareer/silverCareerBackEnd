package com.example.demo.src.product.dto;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

@Builder
public record CreateProduct(
        @NotNull
        String productName,
        @NotNull
        String productDescription,
        @NotNull
        String category,
        @NotNull
        Long price,
        @NotNull
        Long memberIdx,
        String productImages
    ){
}
