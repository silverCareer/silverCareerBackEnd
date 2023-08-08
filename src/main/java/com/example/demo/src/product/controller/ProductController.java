package com.example.demo.src.product.controller;

import com.example.demo.global.exception.BaseResponse;
import com.example.demo.src.product.dto.*;
import com.example.demo.src.product.service.ProductService;
import com.example.demo.utils.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final SecurityUtil securityUtil;

    @GetMapping("/category/{category}")
    public ResponseEntity<DisplayProductRes> displayProductByCategory(@Valid @PathVariable String category) {
        DisplayProductReq productReq = DisplayProductReq.builder().category(category).build();
        DisplayProductRes productRes = productService.displayProductByCategory(productReq);
        return productRes == null
                ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(productRes, HttpStatus.OK);
    }

    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDetailRes> getProductDetail(@Valid @PathVariable Long productId){
        ProductDetailReq productDetailReq = ProductDetailReq.builder().productIdx(productId).build();
        ProductDetailRes productDetailRes = productService.getProductDetail(productDetailReq);
        return new ResponseEntity(productDetailRes, HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public BaseResponse<?> createProduct(@RequestBody @Valid CreateProduct createProduct){
        return securityUtil.getCurrentUsername()
                .map(username -> {
                    productService.createProduct(username, createProduct);
                    return new BaseResponse<>(HttpStatus.OK);
                })
                .orElse(new BaseResponse<>(HttpStatus.UNAUTHORIZED));
    }
}