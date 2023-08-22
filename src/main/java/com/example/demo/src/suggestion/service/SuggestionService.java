package com.example.demo.src.suggestion.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.suggestion.dto.RequestCreateSuggestion;
import org.springframework.http.ResponseEntity;

public interface SuggestionService {
    ResponseEntity<CommonResponse> createSuggestion(final String username, final RequestCreateSuggestion requestCreateSuggestion);
    ResponseEntity<CommonResponse> getMatchCategorySuggestion(final String username);
    ResponseEntity<CommonResponse> getSuggestionDetail(final Long suggestionIdx);

}
