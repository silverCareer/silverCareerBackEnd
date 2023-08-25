package com.example.demo.src.product.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.product.dto.*;
import com.example.demo.src.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    // 카테고리별 상품리스트 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<CommonResponse> displayProductByCategory(
            Authentication authentication,
            @Valid @PathVariable String category,
            @Valid @RequestParam(required = false, defaultValue = "1") int page,
            @Valid @RequestParam(required = false, defaultValue = "1000") int size) {
        return productService.displayProductByCategory(authentication, category, page, size);
    }

    // 특정 상품의 상세정보 조회
    @GetMapping("/detail/{productId}")
    public ResponseEntity<CommonResponse> getProductDetail(Authentication authentication,
                                                           @Valid @PathVariable Long productId){
        return productService.getProductDetail(authentication, productId);
    }

    // 새로운 상품 등록
    @PostMapping(path = "/create", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> createProduct(@AuthenticationPrincipal(expression = "username") String username,
                                                        @RequestPart RequestCreateProduct requestCreateProduct,
                                                        @RequestPart("productImage") MultipartFile productImage) throws IOException {
        return productService.createProduct(username, productImage, requestCreateProduct);
    }

    @GetMapping("/recommend")
    public ResponseEntity<CommonResponse> getRecommendProduct(){
        return productService.getRecommendProduct();
    }
}
