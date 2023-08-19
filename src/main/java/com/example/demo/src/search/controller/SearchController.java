package com.example.demo.src.search.controller;

import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.dto.DisplayProductRes;
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
import java.util.stream.Collectors;

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

        ResponseMultiSearch response = searchService.search(q, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
