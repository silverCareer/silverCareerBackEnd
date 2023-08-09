package com.example.demo.src.suggestion.repository;

import com.example.demo.src.suggestion.domain.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByCategoryAndMember_Category(String category);

}
