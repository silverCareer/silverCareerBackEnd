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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private BidStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member; //멘토들

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "suggestion_idx", nullable = false)
    private Suggestion suggestion;

    @Builder
    public Bid(Long price, BidStatus status, Member member, Suggestion suggestion){
        this.price = price;
        this.status = status;
        this.member = member;
        this.suggestion = suggestion;
    }

    public void updateStatus(BidStatus status){
        this.status = status;
    }
}

