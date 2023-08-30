package com.example.demo.src.product.dto;

import com.example.demo.src.product.domain.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseTop10Products {
    private Long productIdx;
    private String productName;
    private String category;
    private String productImage;

    public static ResponseTop10Products of(Product product){
                return ResponseTop10Products.builder()
                        .productIdx(product.getProductIdx())
                        .productName(product.getProductName())
                        .category(product.getCategory())
                        .productImage(product.getImage())
                        .build();
    }
}
