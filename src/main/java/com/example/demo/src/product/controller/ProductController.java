package com.example.demo.src.product.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.product.dto.*;
import com.example.demo.src.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<ResponseMultiProduct> displayProductByCategory(
            @Valid @PathVariable String category,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "productIdx"));
        ResponseMultiProduct responseMultiProduct = productService.displayProductByCategory(category, pageable);

        return new ResponseEntity<>(responseMultiProduct, HttpStatus.OK);
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
                                                        @RequestPart CreateProductReq createProductReq,
                                                        @RequestPart("productImage") MultipartFile productImage) throws IOException {
            productService.createProduct(username, productImage, createProductReq);

            return ResponseEntity.ok().body(CommonResponse.builder()
                    .success(true)
                    .response("상품등록 성공")
                    .build());
    }
}
