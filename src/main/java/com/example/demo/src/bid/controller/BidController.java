package com.example.demo.src.bid.controller;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.bid.dto.RequestBid;
import com.example.demo.src.bid.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BidController {
    private final BidService bidService;

    // 입찰 등록
    @PostMapping("/suggestion/{suggestionIdx}/bid")
    @PreAuthorize("hasAnyRole('ROLE_MENTOR')")
    public ResponseEntity<CommonResponse> registerBid(@AuthenticationPrincipal(expression = "username") String memberEmail,
                                                      @Valid @PathVariable Long suggestionIdx,
                                                      @Valid @RequestBody RequestBid bidDto) {
        return bidService.registerBid(memberEmail, suggestionIdx, bidDto);
    }

    // 입찰 리스트 확인
    @GetMapping("/bid")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> getRegisterBidList(@AuthenticationPrincipal(expression = "username") String memberEmail) {
        return bidService.getRegisterBidList(memberEmail);
    }

    // 입찰 상세정보
    @GetMapping("/bid/{bidIdx}")
    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
    public ResponseEntity<CommonResponse> getRegisterBidsDetail(@Valid @PathVariable Long bidIdx) {
        return bidService.getRegisterBidsDetail(bidIdx);
    }

//    // 입찰 수락
//    @PostMapping("/bid/{bidIdx}/confirm")
//    @PreAuthorize("hasAnyRole('ROLE_MENTEE')")
//    public ResponseEntity<CommonResponse> acceptBidOfSuggestion(@AuthenticationPrincipal(expression = "username") String memberEmail,
//                                                                @Valid @PathVariable Long bidIdx) {
//        return bidServiceImpl.acceptBidOfSuggestion(memberEmail, bidIdx);
//    }

}
