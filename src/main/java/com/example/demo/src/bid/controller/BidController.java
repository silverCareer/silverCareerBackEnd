package com.example.demo.src.bid.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.bid.dto.RequestBid;
import com.example.demo.src.bid.dto.ResponseBid;
import com.example.demo.src.bid.service.BidServiceImpl;
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
public class BidController {
    private final BidServiceImpl bidServiceImpl;

    @PostMapping("/suggestion/{suggestionIdx}/bid")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> registerBid(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                                      @Valid @PathVariable Long suggestionIdx
            , @Valid @RequestBody RequestBid bidDto) {
        bidServiceImpl.registerBid(memberEmail, suggestionIdx, bidDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("의뢰 입찰 가격 등록 성공입니다.")
                .build());
    }

    @GetMapping("/bid")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> getRegisterBidList(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        List<ResponseBid> res = bidServiceImpl.getRegisterBidList(memberEmail);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(res)
                .build());
    }

    @GetMapping("/bid/{bidIdx}")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> getRegisterBidsDetail(@AuthenticationPrincipal(expression = "username") String memberEmail
            , @Valid @PathVariable Long bidIdx) {
        ResponseBid res = bidServiceImpl.getRegisterBidsDetail(bidIdx);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response(res)
                .build());
    }

    @PostMapping("/bid/{bidIdx}/confirm")
    public ResponseEntity<CommonResponse> acceptBidOfSuggestion(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                                                @Valid @PathVariable Long bidIdx) {
        bidServiceImpl.acceptBidOfSuggestion(memberEmail,bidIdx);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .success(true)
                .response("의뢰 수락되었습니다.")
                .build());
    }

    // 결제 API -> Payment에서 진행
}
