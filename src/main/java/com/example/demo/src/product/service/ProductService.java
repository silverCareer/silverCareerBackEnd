package com.example.demo.src.product.service;

import com.example.demo.src.product.dto.*;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    void createProduct(String username, CreateProductReq createProductReq) throws IOException;
    List<DisplayProductRes> displayProductByCategory(String category);
    ProductDetailRes getProductDetail(Long productId);
}
