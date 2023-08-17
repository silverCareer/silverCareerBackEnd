package com.example.demo.src.suggestion.service;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.error.CustomException;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.suggestion.domain.Suggestion;
import com.example.demo.src.suggestion.dto.RequestCreateSuggestion;
import com.example.demo.src.suggestion.dto.ResponseSuggestion;
import com.example.demo.src.suggestion.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createSuggestion(final String username, final RequestCreateSuggestion suggestionDto) {
        Member mentee = memberRepository.findMemberByUsername(username);
        Suggestion suggestion = Suggestion.builder()
                .title((suggestionDto.getTitle()))
                .description(suggestionDto.getDescription())
                .category(suggestionDto.getCategory())
                .price(suggestionDto.getPrice())
                .member(mentee)
                .build();
        suggestionRepository.save(suggestion);
        mentee.addSuggestion(suggestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseSuggestion> getMatchCategorySuggestion(final String username) {
        Member mentor = memberRepository.findMemberByUsername(username);
        List<Suggestion> suggestions = suggestionRepository.findByCategory(mentor.getCategory());

        if (suggestions.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }
        return suggestions.stream()
                .map(ResponseSuggestion::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseSuggestion getSuggestionDetail(final Long suggestionIdx) {
        Suggestion suggestion = suggestionRepository.findById(suggestionIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        return ResponseSuggestion.builder()
                .suggestionIdx(suggestion.getSuggestionIdx())
                .title(suggestion.getTitle())
                .description(suggestion.getDescription())
                .category(suggestion.getCategory())
                .price(suggestion.getPrice())
                .memberName(suggestion.getMember().getName())
                .build();
    }

}
