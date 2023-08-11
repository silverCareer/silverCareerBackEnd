package com.example.demo.src.suggestion.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.suggestion.dto.RequestCreateSuggestion;
import com.example.demo.src.suggestion.dto.ResponseSuggestion;
import com.example.demo.src.suggestion.service.SuggestionServiceImpl;
import com.example.demo.utils.SecurityUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SuggestionController {
    private final SuggestionServiceImpl suggestionServiceImpl;
    private final SecurityUtil securityUtil;

    @GetMapping("/suggestion")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> getMatchCategorySuggestion(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        List<ResponseSuggestion> res = suggestionServiceImpl.getMatchCategorySuggestion(memberEmail);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(res)
                .build());
    }

    @GetMapping("/suggestion/{suggestionIdx}")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> getSuggestionDetail(@AuthenticationPrincipal(expression = "username") String memberEmail, @Valid @PathVariable Long suggestionIdx) {
        ResponseSuggestion res = suggestionServiceImpl.getSuggestionDetail(suggestionIdx);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(res)
                .build());
    }

    @PostMapping("/suggestion/register")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> createSuggestion(@AuthenticationPrincipal(expression = "username") String memberEmail, @Valid @RequestBody RequestCreateSuggestion requestCreateSuggestion) {
        suggestionServiceImpl.createSuggestion(memberEmail, requestCreateSuggestion);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("의뢰 등록 성공입니다.")
                .build());
    }
}
