package com.example.demo.src.product.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class CreateProductReq {
        private String productName;
        private String productDescription;
        private String category;
        private Long price;
        private MultipartFile productImage;
}
