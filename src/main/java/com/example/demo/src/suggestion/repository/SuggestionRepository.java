package com.example.demo.src.suggestion.repository;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.suggestion.domain.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    List<Suggestion> findByCategory(String category);
    Suggestion findByMemberName(String email);
    Optional<Suggestion> findByTitle(String title);

    @Modifying
    @Query("SELECT DISTINCT s FROM Suggestion s JOIN s.bids b WHERE b.status = '완료' AND b.member = :member")
    List<Suggestion> findSuggestionsWithCompleteBidsAndMember(Member member);

    @Modifying
    @Query("SELECT DISTINCT s FROM Suggestion s JOIN s.bids b WHERE b.status = '진행중' AND b.member = :member")
    List<Suggestion> findSuggestionsWithInCompleteBidsAndMember(Member member);

    @Modifying
    @Query("SELECT DISTINCT s FROM Suggestion  s WHERE s.isTerminated = false AND s.category = :category")
    List<Suggestion> findUnterminatedSuggestionsByCategory(String category);
}
