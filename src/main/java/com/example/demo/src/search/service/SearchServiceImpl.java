package com.example.demo.src.search.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.search.NotFoundSearchException;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.ResponseDisplayProducts;
import com.example.demo.src.product.repository.ProductRepository;
import com.example.demo.src.search.dto.ResponseMultiSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> search(String query, int page, int size){
        Page<Product> searchProduct = productRepository.findByProductName(query, PageRequest.of(page - 1, size, Sort.by("productIdx").descending()));
        if(searchProduct.isEmpty() || searchProduct == null){
            throw new NotFoundSearchException();
        }
        List<Product> medias = searchProduct.getContent();
        long totalResults = searchProduct.getTotalElements();
        List<ResponseDisplayProducts> searchListDto = mapToDisplayProductRes(medias);
        ResponseMultiSearch<ResponseDisplayProducts> response =
                new ResponseMultiSearch<>(searchListDto, page, searchProduct.getTotalPages(), totalResults);
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

    private List<ResponseDisplayProducts> mapToDisplayProductRes(List<Product> products) {
        return products.stream()
                .map(ResponseDisplayProducts::of)
                .collect(Collectors.toList());
    }
}
