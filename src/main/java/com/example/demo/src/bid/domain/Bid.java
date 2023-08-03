package com.example.demo.src.bid.domain;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.request.domain.Request;
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
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_idx", nullable = false)
    private Request request;

    @Builder
    public Bid(Long price, Member member, Request request){
        this.price = price;
        this.member = member;
        this.request = request;
    }
}

