package com.example.demo.src.bid.service;

import com.example.demo.src.bid.dto.RequestBid;
import com.example.demo.src.bid.dto.ResponseBid;

import java.util.List;

public interface BidService {
    void registerBid(final String username, final Long suggestionIdx, final RequestBid bidDto);
    List<ResponseBid> getRegisterBidList(final String username);
    ResponseBid getRegisterBidsDetail(final Long bidIdx);
    void acceptBidOfSuggestion(final String memberEmail, final Long bidIdx);
}
