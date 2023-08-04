package com.example.demo.src.review.domain;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.product.domain.Product;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_idx")
    private Long reviewIdx;

    @Column(name = "context", length = 50, nullable = false)
    private String context;

    @Column(name = "images", columnDefinition = "json")
    private String images;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_idx", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @Builder
    public Review(String context, String images, Product product, Member member){
        this.context = context;
        this.images = images;
        this.product = product;
        this.member = member;
    }
}