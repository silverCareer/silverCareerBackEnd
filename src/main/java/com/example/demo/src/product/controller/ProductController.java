package com.example.demo.src.product.controller;

import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.S3Service;
import com.example.demo.src.product.dto.*;
import com.example.demo.src.product.service.ProductService;
import com.example.demo.utils.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/category/{category}")
    public ResponseEntity<CommonResponse> displayProductByCategory(@Valid @PathVariable String category) {
        List<DisplayProductRes> displayProductRes = productService.displayProductByCategory(category);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(displayProductRes)
                .build());
    }

    @GetMapping("/detail/{productId}")
    public ResponseEntity<CommonResponse> getProductDetail(@Valid @PathVariable Long productId){
        ProductDetailRes productDetailRes = productService.getProductDetail(productId);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(productDetailRes)
                .build());
    }

    @PostMapping(path = "/create", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> createProduct(@AuthenticationPrincipal(expression = "username") String username,
                                                        @RequestParam("productName") String productName,
                                                        @RequestParam("productDescription") String productDescription,
                                                        @RequestParam("category") String category,
                                                        @RequestParam("price") Long price,
                                                        @RequestParam("productImage") MultipartFile productImage) throws IOException {
            CreateProductReq createProductReq = CreateProductReq.builder()
                    .productName(productName)
                    .productDescription(productDescription)
                    .category(category)
                    .price(price)
                    .productImage(productImage)
                    .build();
            productService.createProduct(username, createProductReq);
            return ResponseEntity.ok().body(CommonResponse.builder()
                    .success(true)
                    .response("상품등록 성공")
                    .build());
    }
}
