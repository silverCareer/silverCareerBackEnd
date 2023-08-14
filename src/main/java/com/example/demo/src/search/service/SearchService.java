package com.example.demo.src.search.service;

import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;

    public Page<Product> search(String query, int page, int size){

        return productRepository.findByProductName(query, PageRequest.of(page, size, Sort.by("productIdx").descending()));
    }
}
