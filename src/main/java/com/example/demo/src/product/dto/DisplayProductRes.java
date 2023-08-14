package com.example.demo.src.product.dto;

import com.example.demo.src.product.domain.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayProductRes{
    private Long productIdx;
    private String productName;
    private String productDescription;
    private String productImage;
    private Long productLikes;
    private Long productPrice;

    public static DisplayProductRes of(Product product){
        return DisplayProductRes.builder()
                .productIdx(product.getProductIdx())
                .productName(product.getProductName())
                .productDescription(product.getDescription())
                .productImage(product.getImage())
                .productLikes(product.getLikes())
                .productPrice(product.getPrice())
                .build();
    }
}
