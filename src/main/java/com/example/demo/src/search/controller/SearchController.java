package com.example.demo.src.search.controller;

import com.example.demo.src.product.domain.Product;
import com.example.demo.src.search.dto.ResponseMultiSearch;
import com.example.demo.src.search.mapper.SearchMapper;
import com.example.demo.src.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {
    private final SearchService searchService;
    private final SearchMapper searchMapper;

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam String q,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "100") int size){

        Page<Product> pageMedias = searchService.search(q, page-1, size);
        List<Product> medias = pageMedias.getContent();

        // 총 결과 수를 가져옵니다.
        long totalResults = pageMedias.getTotalElements();

        return new ResponseEntity<>(new ResponseMultiSearch(searchMapper.displayProductRes(medias), page, pageMedias.getTotalPages(), totalResults), HttpStatus.OK);
    }
}
