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
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createSuggestion(final String username, final RequestCreateSuggestion dto) {
        Member member = memberRepository.findMemberByUsername(username);
        Suggestion suggestion = Suggestion.builder()
                .description(dto.description())
                .category(dto.category())
                .price(dto.price())
                .member(member)
                .build();
        suggestionRepository.save(suggestion);
        member.addSuggestion(suggestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseSuggestion> getMatchCategorySuggestion(final String username) {
        Member mentor = memberRepository.findMemberByUsername(username);
        List<Suggestion> suggestions = suggestionRepository.findByCategoryAndMember_Category(mentor.getCategory());
        if (suggestions.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ELEMENT);
        }
        return suggestions.stream()
                .map(ResponseSuggestion::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseSuggestion getSuggestionDetail(final Long suggestionIdx) {
        Optional<Suggestion> OptionalSuggestion = suggestionRepository.findById(suggestionIdx);
        Suggestion suggestion = OptionalSuggestion.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
        return ResponseSuggestion.builder()
                .suggestionIdx(suggestion.getSuggestionIdx())
                .description(suggestion.getDescription())
                .category(suggestion.getCategory())
                .price(suggestion.getPrice())
                .memberName(suggestion.getMember().getName())
                .build();
    }

}
