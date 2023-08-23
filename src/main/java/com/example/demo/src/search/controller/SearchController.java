package com.example.demo.src.search.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<CommonResponse> search(@RequestParam String q,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "100") int size){

        return searchService.search(q, page, size);
    }
}
