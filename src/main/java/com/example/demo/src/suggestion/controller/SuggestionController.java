package com.example.demo.src.suggestion.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.suggestion.dto.RequestCreateSuggestion;
import com.example.demo.src.suggestion.service.SuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SuggestionController {
    private final SuggestionService suggestionService;

    // 멘토의 카테고리와 일치하는 의뢰 목록
    @GetMapping("/suggestion")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> getMatchCategorySuggestion(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        return suggestionService.getMatchCategorySuggestion(memberEmail);
    }

    // 의뢰 상세정보 조회 -> 입찰 등록 페이지에 사용
    @GetMapping("/suggestion/{suggestionIdx}")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> getSuggestionDetail(@Valid @PathVariable Long suggestionIdx) {
        return suggestionService.getSuggestionDetail(suggestionIdx);
    }

    @PostMapping("/suggestion/register")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> createSuggestion(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                                           @Valid @RequestBody RequestCreateSuggestion requestCreateSuggestion) {
        return suggestionService.createSuggestion(memberEmail, requestCreateSuggestion);
    }
}
