package com.example.demo.src.suggestion.domain;

import com.example.demo.src.bid.domain.Bid;
import com.example.demo.src.member.domain.Member;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "isTerminated", columnDefinition = "BOOL DEFAULT false")
    boolean isTerminated;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member; //멘티들

    @OneToMany(mappedBy = "suggestion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Bid> bids = new ArrayList<>();

    @Builder
    public Suggestion(String title, String description, String category, Long price, boolean isTerminated, Member member, boolean isAccept) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.isTerminated = isTerminated;
        this.member = member;
    }

    public void terminateSuggestion(boolean status){
        this.isTerminated = status;
    }
}
