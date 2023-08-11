package com.example.demo.src.bid.domain;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.suggestion.domain.Suggestion;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "bid")
@Getter
@NoArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_idx")
    private Long bidIdx;

    @Column(name = "price", nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_idx", nullable = false)
    private Suggestion suggestion;

    @Builder
    public Bid(Long price, Member member, Suggestion suggestion){
        this.price = price;
        this.member = member;
        this.suggestion = suggestion;
    }
}

