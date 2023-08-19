package com.example.demo.src.bid.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.bid.domain.Bid;
import com.example.demo.src.bid.domain.BidStatus;
import com.example.demo.src.bid.dto.RequestBid;
import com.example.demo.src.bid.dto.ResponseBid;
import com.example.demo.src.bid.repository.BidRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.suggestion.domain.Suggestion;
import com.example.demo.src.suggestion.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final MemberRepository memberRepository;
    private final SuggestionRepository suggestionRepository;
    private final BidRepository bidRepository;

    @Override
    @Transactional
    public void registerBid(final String username, final Long suggestionIdx, final RequestBid bidDto) {
        Member mentor = memberRepository.findMemberByUsername(username);
        Suggestion suggestion = suggestionRepository.findById(suggestionIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Member mentee = memberRepository.findMemberByUsername(suggestion.getMember().getUsername());

        Bid bid = Bid.builder()
                .price(bidDto.getPrice())
                .status(BidStatus.진행중)
                .suggestion(suggestion)
                .member(mentor)
                .build();
        bidRepository.save(bid);
        mentor.addBid(bid);
        mentee.updateAlarmStatus(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseBid> getRegisterBidList(final String username) { //멘티가 확인하는 입찰 내역들
        Suggestion suggestion = suggestionRepository.findByMemberName(username);
        if (suggestion == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }
        List<Bid> bids = bidRepository.findAll();
        return bids.stream().map(ResponseBid::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseBid getRegisterBidsDetail(final Long bidIdx) {
        Bid bid = bidRepository.findById(bidIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        return ResponseBid.builder()
                .memberName(bid.getMember().getName())
                .price(bid.getPrice())
                .build();
    }

    @Override
    @Transactional
    public void acceptBidOfSuggestion(final String memberEmail, final Long bidIdx) { //멘티가 입찰한 멘토의 입찰건 중 하나를 채택하기
        Bid bid = bidRepository.findByBidIdx(bidIdx);
        if (bid == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }
        bid.updateStatus(BidStatus.완료);
        List<Bid> otherBids = bidRepository.findAll();
        List<Long> bidIdxToDelete = otherBids.stream()
                .filter(bids -> !bids.getBidIdx().equals(bid.getBidIdx()))
                .map(Bid::getBidIdx)
                .collect(Collectors.toList());
        if (!bidIdxToDelete.isEmpty()) {
            bidRepository.deleteBidsByIdIn(bidIdxToDelete);
        }
    }
}
