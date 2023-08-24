package com.example.demo.src.product.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.product.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ResponseEntity<CommonResponse> createProduct(final String username, final MultipartFile image, final RequestCreateProduct requestCreateProduct) throws IOException;

    ResponseEntity<CommonResponse> displayProductByCategory(Authentication authentication, final String category, final int page, final int size);

    ResponseEntity<CommonResponse> getProductDetail(Authentication authentication, final Long productId);
}
