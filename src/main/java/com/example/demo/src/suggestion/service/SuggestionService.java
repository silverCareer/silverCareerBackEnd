package com.example.demo.src.suggestion.service;

import com.example.demo.src.suggestion.dto.RequestCreateSuggestion;
import com.example.demo.src.suggestion.dto.ResponseSuggestion;

import java.util.List;

public interface SuggestionService {
    void createSuggestion(final String username, final RequestCreateSuggestion requestCreateSuggestion);
    List<ResponseSuggestion> getMatchCategorySuggestion(final String username);
    ResponseSuggestion getSuggestionDetail(final Long suggestionIdx);

}
