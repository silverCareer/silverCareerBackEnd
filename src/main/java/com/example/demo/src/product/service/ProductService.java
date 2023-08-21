package com.example.demo.src.product.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.product.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ResponseEntity<CommonResponse> createProduct(String username, MultipartFile image, RequestCreateProduct requestCreateProduct) throws IOException;
//    List<DisplayProductRes> displayProductByCategory(String category, Pageable pageable);

    ResponseEntity<CommonResponse> displayProductByCategory(String category, int page, int size);
    ResponseEntity<CommonResponse> getProductDetail(Authentication authentication, Long productId);
}
