package com.example.demo.src.search.service;

import com.example.demo.global.exception.dto.CommonResponse;
import org.springframework.http.ResponseEntity;

public interface SearchService {

    ResponseEntity<CommonResponse> search(String q, int page, int size);
}
