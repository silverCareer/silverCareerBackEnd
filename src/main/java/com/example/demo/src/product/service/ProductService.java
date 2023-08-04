package com.example.demo.src.product.service;

import com.example.demo.src.product.dto.CreateProduct;
import com.example.demo.src.product.dto.DisplayProductReq;
import com.example.demo.src.product.dto.DisplayProductRes;

public interface ProductService {
    void createProduct(CreateProduct createProduct);
    DisplayProductRes displayProductByCategory(DisplayProductReq displayProductReq);
}
