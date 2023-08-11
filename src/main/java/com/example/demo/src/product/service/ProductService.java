package com.example.demo.src.product.service;

import com.example.demo.src.product.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    void createProduct(String username, MultipartFile image, CreateProductReq createProductReq) throws IOException;
    List<DisplayProductRes> displayProductByCategory(String category);
    ProductDetailRes getProductDetail(Long productId);
}
