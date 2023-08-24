package com.example.demo.src.search.service;

import com.example.demo.global.exception.dto.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface SearchService {

    ResponseEntity<CommonResponse> search(final Authentication authentication, final String q, final int page, final int size);
}
