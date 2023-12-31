package com.example.demo.src.product.dto;

import com.example.demo.src.product.domain.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDisplayProducts {
    private Long productIdx;
    private String productName;
    private String productDescription;
    private String category;
    private String productImage;
    private boolean isLiked;
    private Long productLikes;
    private Long productPrice;

    public static ResponseDisplayProducts of(Product product, boolean isLiked){
        return ResponseDisplayProducts.builder()
                .productIdx(product.getProductIdx())
                .productName(product.getProductName())
                .productDescription(product.getDescription())
                .category(product.getCategory())
                .productImage(product.getImage())
                .isLiked(isLiked)
                .productLikes(product.getLikes())
                .productPrice(product.getPrice())
                .build();
    }
}
