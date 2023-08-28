package com.example.demo.src.bid.repository;

import com.example.demo.src.bid.domain.Bid;
import com.example.demo.src.bid.domain.BidStatus;
import com.example.demo.src.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("DELETE FROM Bid b WHERE b.bidIdx IN :bidIds")
    void deleteBidsByIdIn(@Param("bidIds") List<Long> bidIds);

    List<Bid> findBidsBySuggestion_SuggestionIdx(Long suggestionIdx);

    List<Bid> findBidBySuggestionMemberAndStatus(Member member, BidStatus status);

    Optional<Bid> findBidsByBidIdx(Long bidIdx);
}