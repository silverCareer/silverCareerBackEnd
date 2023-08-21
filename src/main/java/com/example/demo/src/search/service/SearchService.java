package com.example.demo.src.search.service;

import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.ResponseDisplayProducts;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.search.dto.ResponseMultiSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;

    public ResponseMultiSearch search(String query, int page, int size) {
        Page<Product> pageMedias = productRepository.findByProductName(query, PageRequest.of(page - 1, size, Sort.by("productIdx").descending()));
        List<Product> medias = pageMedias.getContent();
        long totalResults = pageMedias.getTotalElements();
        List<ResponseDisplayProducts> response = mapToDisplayProductRes(medias);

        return new ResponseMultiSearch(response, page, pageMedias.getTotalPages(), totalResults);
    }

    private List<ResponseDisplayProducts> mapToDisplayProductRes(List<Product> products) {
        return products.stream()
                .map(ResponseDisplayProducts::of)
                .collect(Collectors.toList());
    }
}
