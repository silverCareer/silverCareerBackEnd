package com.example.demo.src.bid.repository;

import com.example.demo.src.bid.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Bid findBidByMember(String email);
    List<Bid> findBidBySuggestionIdx(Long suggestionIdx);
    List<Bid> findBidBySuggestionIdxAndIsAcceptedFalse(Long suggestionIdx);
//    Bid findBidByMemberEmailAndSuggestionIdx(String email, Long suggestionIdx);
}
