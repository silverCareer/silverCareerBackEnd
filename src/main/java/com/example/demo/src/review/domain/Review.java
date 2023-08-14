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

    @Column(name = "post_date", length = 50, nullable = false)
    private String postDate;

    @Column(name = "rating", columnDefinition = "BIGINT", nullable = false)
    private Long rating;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_idx", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Builder
    public Review(String context, String postDate, Long rating, Product product, Member member){
        this.context = context;
        this.postDate = postDate;
        this.rating = rating;
        this.product = product;
        this.member = member;
    }
}