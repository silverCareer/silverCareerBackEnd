package com.example.demo.src.bid.controller;

import com.amazonaws.Response;
import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.bid.service.BidServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BidController {
    private final BidServiceImpl bidServiceImpl;

    @PostMapping("/suggestion/{suggestionIdx}/bid")
    public ResponseEntity<CommonResponse> registerBid(@AuthenticationPrincipal(expression = "username") String memberEmail, @Valid @PathVariable Long suggestionIdx,
                                                      ) {
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("의뢰 입찰 가격 등록 성공입니다.")
                .build());
    }

    @PostMapping("/")
    public ResponseEntity<CommonResponse> acceptBidOfSuggestion() {
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("의뢰가 성사되었습니다.")
                .build());
    }
}
