package com.example.demo.src.suggestion.domain;

import com.example.demo.src.member.domain.Member;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "suggestion")
@Getter
@NoArgsConstructor
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suggestion_idx")
    private Long suggestionIdx;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "category", length = 20, nullable = false)
    private String category;

    @Column(name = "price", nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Builder
    public Suggestion(String description, String category, Long price, Member member){
        this.description = description;
        this.category = category;
        this.price = price;
        this.member = member;
    }
}