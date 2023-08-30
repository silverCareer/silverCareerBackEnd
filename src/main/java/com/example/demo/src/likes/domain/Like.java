package com.example.demo.src.likes.domain;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.product.domain.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @Column(name = "likes_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likesIdx;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_idx", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Builder
    public Like(Product product, Member member) {
        this.product = product;
        this.member = member;
    }
}
