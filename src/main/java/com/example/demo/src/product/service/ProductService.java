package com.example.demo.src.product.service;

import com.example.demo.src.product.dto.*;

public interface ProductService {
    void createProduct(String username, CreateProduct createProduct);
    DisplayProductRes displayProductByCategory(DisplayProductReq displayProductReq);
    ProductDetailRes getProductDetail(ProductDetailReq productDetailReq);
}
