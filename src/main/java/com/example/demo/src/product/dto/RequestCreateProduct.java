package com.example.demo.src.product.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestCreateProduct {
        private String productName;
        private String productDescription;
        private String category;
        private String address;
        private Long price;
}
