package com.example.demo.src.bid.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.src.bid.dto.RequestBid;
import org.springframework.http.ResponseEntity;

public interface BidService {
    ResponseEntity<CommonResponse> registerBid(final String memberEmail, final Long suggestionIdx, final RequestBid bidDto);
    ResponseEntity<CommonResponse> getRegisterBidList(final String memberEmail);
    ResponseEntity<CommonResponse> getRegisterBidsDetail(final Long bidIdx);
}
