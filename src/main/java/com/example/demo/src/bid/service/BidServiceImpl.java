package com.example.demo.src.bid.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.bid.domain.Bid;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final MemberRepository memberRepository;
    private final SuggestionRepository suggestionRepository;
    private final BidRepository bidRepository;

    @Transactional
    public void registerBid(final String username, final Long suggestionIdx, final RequestBid bidDto) {
        Member mentor = memberRepository.findMemberByUsername(username);
        Optional<Suggestion> optionalSuggestion = suggestionRepository.findById(suggestionIdx);
        Suggestion suggestion = optionalSuggestion.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        Bid bid = bidRepository.findBidByMember(username);
        if (bid != null) {
            throw new CustomException(ErrorCode.DUPLICATE_BID_REGISTER_EXCEPTION);
        }
        bid = Bid.builder()
                .price(bidDto.getPrice())
                .suggestion(suggestion)
                .member(mentor)
                .build();
        bidRepository.save(bid);
        mentor.addBid(bid);
    }

    @Transactional(readOnly = true)
    public List<ResponseBid> getRegisterBidList(final String username) { //멘티가 확인하는 입찰 내역들
        Suggestion suggestion = suggestionRepository.findSuggestionByMember(username);
        if (suggestion == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }
        List<Bid> bids = bidRepository.findBidBySuggestionIdx(suggestion.getSuggestionIdx());

        return bids.stream().map(ResponseBid::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseBid getRegisterBidsDetail(final Long bidIdx) {
        Optional<Bid> optionalBid = bidRepository.findById(bidIdx);
        Bid bid = optionalBid.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        return ResponseBid.builder().memberName(bid.getMember().getName()).price(bid.getPrice()).build();
    }

    @Transactional
    public void acceptBidOfSuggestion(final String memberEmail, final Long bidIdx) {
        Optional<Bid> bid = bidRepository.findById(bidIdx);
        if (!bid.isPresent()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }
        List<Bid> otherBids = bidRepository.findBidBySuggestionIdx(bid.get().getSuggestion().getSuggestionIdx());



    }


}
