package com.example.demo.src.suggestion.service;

import com.example.demo.global.exception.dto.CommonResponse;
import com.example.demo.global.exception.error.member.NotFoundMemberException;
import com.example.demo.global.exception.error.suggestion.NotFoundSuggestionException;
import com.example.demo.global.exception.error.suggestion.NotFoundSuggestionsException;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.suggestion.domain.Suggestion;
import com.example.demo.src.suggestion.dto.RequestCreateSuggestion;
import com.example.demo.src.suggestion.dto.ResponseSuggestion;
import com.example.demo.src.suggestion.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse> createSuggestion(final String username, final RequestCreateSuggestion suggestionDto) {
        Member mentee = memberRepository.findByUsername(username)
                .orElseThrow(NotFoundMemberException::new);
        Suggestion suggestion = Suggestion.builder()
                .title((suggestionDto.getTitle()))
                .description(suggestionDto.getDescription())
                .category(suggestionDto.getCategory())
                .price(suggestionDto.getPrice())
                .member(mentee)
                .build();
        List<Member> mentors = memberRepository.findMembersByCategory(suggestionDto.getCategory());
        suggestionRepository.save(suggestion);
        mentee.addSuggestion(suggestion);
        mentors.forEach(member -> member.updateAlarmStatus(true));
        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response("의뢰 등록 성공").build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> getMatchCategorySuggestion(final String username) {
        Member mentor = memberRepository.findByUsername(username)
                .orElseThrow(NotFoundMemberException::new);

        List<Suggestion> suggestions = suggestionRepository.findByCategory(mentor.getCategory())
                .orElseThrow(NotFoundSuggestionsException::new);
        if(suggestions.isEmpty()){throw new NotFoundSuggestionsException();}

        List<ResponseSuggestion> response = suggestions.stream().map(ResponseSuggestion::of).collect(Collectors.toList());

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommonResponse> getSuggestionDetail(final Long suggestionIdx) {
        Suggestion suggestion = suggestionRepository.findById(suggestionIdx)
                .orElseThrow(NotFoundSuggestionException::new);

        ResponseSuggestion response = ResponseSuggestion.builder()
                .suggestionIdx(suggestion.getSuggestionIdx())
                .title(suggestion.getTitle())
                .description(suggestion.getDescription())
                .category(suggestion.getCategory())
                .price(suggestion.getPrice())
                .suggester(suggestion.getMember().getName())
                .build();

        return ResponseEntity.ok().body(
                CommonResponse.builder().success(true).response(response).build());
    }

}
