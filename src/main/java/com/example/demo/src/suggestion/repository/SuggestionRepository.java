package com.example.demo.src.suggestion.repository;

import com.example.demo.src.suggestion.domain.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    List<Suggestion> findByCategory(String category);
    Suggestion findByMemberName(String email);
    Optional<Suggestion> findByTitle(String title);
}
