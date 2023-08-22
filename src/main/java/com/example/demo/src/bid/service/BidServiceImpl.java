package com.example.demo.src.bid.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.bid.NotFoundBidException;
import com.example.demo.global.exception.error.bid.NotFoundBidsException;
import com.example.demo.global.exception.error.member.NotFoundMemberException;
import com.example.demo.global.exception.error.suggestion.NotFoundSuggestionException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public ResponseEntity<CommonResponse> registerBid(final String username, final Long suggestionIdx, final RequestBid bidDto) {
        Member mentor = memberRepository.findByUsername(username)
                .orElseThrow(NotFoundMemberException::new);

        Suggestion suggestion = suggestionRepository.findSuggestionBySuggestionIdx(suggestionIdx)
                .orElseThrow(NotFoundSuggestionException::new);
      
        Member mentee = memberRepository.findByUsername(suggestion.getMember().getUsername())
                .orElseThrow(NotFoundMemberException::new);

        Bid bid = Bid.builder()
                .price(bidDto.getPrice())
                .status(BidStatus.진행중)
                .suggestion(suggestion)
                .member(mentor)
                .build();
        bidRepository.save(bid);
        mentor.addBid(bid);
        mentee.updateAlarmStatus(true);

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("입찰 등록 성공").build());
    }

    //멘티가 확인하는 입찰 내역들
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> getRegisterBidList(final String username) {
        List<Suggestion> mySuggestion = suggestionRepository.findSuggestionsByMemberUsername(username)
                .orElseThrow(NotFoundSuggestionException::new);
        List<Bid> bids = new ArrayList<>();
        mySuggestion.forEach(suggestion ->
                bidRepository.findBidsBySuggestion_SuggestionIdx(suggestion.getSuggestionIdx())
                        .forEach(bid -> bids.add(bid)));
        if(bids.isEmpty()){throw new NotFoundBidsException();}
        List<ResponseBid> response = bids.stream().map(ResponseBid::of).collect(Collectors.toList());

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> getRegisterBidsDetail(final Long bidIdx) {
        Bid bid = bidRepository.findById(bidIdx).orElseThrow(NotFoundBidException::new);
        ResponseBid response = ResponseBid.builder()
                .bidIdx(bid.getBidIdx())
                .title(bid.getSuggestion().getTitle())
                .category(bid.getSuggestion().getCategory())
                .price(bid.getPrice())
                .mentorName(bid.getMember().getName())
                .build();
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

    @Override
    @Transactional
    public ResponseEntity<CommonResponse> acceptBidOfSuggestion(final String memberEmail, final Long bidIdx) { //멘티가 입찰한 멘토의 입찰건 중 하나를 채택하기
        Bid bid = bidRepository.findById(bidIdx).orElseThrow(NotFoundBidException::new);
        Suggestion suggestion = bid.getSuggestion();
        bid.updateStatus(BidStatus.완료);
        suggestion.terminateSuggestion(true);
        
        List<Bid> otherBids = bidRepository.findBidsBySuggestion_SuggestionIdx(suggestion.getSuggestionIdx());
        
        List<Long> bidIdxToDelete = otherBids.stream()
                .filter(Bid -> !Bid.getBidIdx().equals(bid.getBidIdx()))
                .map(Bid::getBidIdx)
                .collect(Collectors.toList());
      
        if (!bidIdxToDelete.isEmpty()) {
            bidRepository.deleteBidsByIdIn(bidIdxToDelete);
        }
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).
                        response(String.format("%s님의 '%s' 의뢰에 관한 입찰 수락완료", bid.getMember().getName(), suggestion.getTitle())).build());
    }
}
