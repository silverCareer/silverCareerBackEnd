package com.example.demo.src.product.controller;

import com.example.demo.global.exception.BaseResponse;
import com.example.demo.src.product.dto.CreateProduct;
import com.example.demo.src.product.dto.DisplayProductReq;
import com.example.demo.src.product.dto.DisplayProductRes;
import com.example.demo.src.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<DisplayProductRes> displayProductByCategory(@Valid @PathVariable String category) {
        DisplayProductReq productReq = DisplayProductReq.builder().category(category).build();
        DisplayProductRes productRes = productService.displayProductByCategory(productReq);
        return new ResponseEntity<>(productRes, HttpStatus.OK);
    }

    @PostMapping("/create")
    public BaseResponse<?> createProduct(@RequestBody @Valid CreateProduct createProduct){
        productService.createProduct(createProduct);
        return new BaseResponse<>(HttpStatus.OK);
    }
}
