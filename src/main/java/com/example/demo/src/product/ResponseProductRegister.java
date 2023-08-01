package com.example.demo.src.product;

import com.example.demo.src.product.Product;
import lombok.Builder;

@Builder
public record ResponseProductRegister(
        Long prodIdx,
        String productName,
        String description,
        String categoryName,
        String image,
        Long saleCount,
        Long likes

)
{
    public static ResponseProductRegister of(Product product) {
        if (product == null) return null;
        return ResponseProductRegister.builder()
                .prodIdx(product.getProdIdx())
                .productName(product.getProductName())
                .description(product.getDescription())
                .categoryName(product.getCategoryName())
                .image(product.getImage())
                .saleCount(product.getSaleCount())
                .likes(product.getLikes())
                .build();
    }
}
