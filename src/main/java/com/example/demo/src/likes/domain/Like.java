package com.example.demo.src.likes.domain;

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

    @Column(name = "product_idx")
    private Long productIdx;

    @Column(name = "email")
    private String memberEmail;

    @Builder
    public Like(Long productIdx, String memberEmail) {
        this.productIdx = productIdx;
        this.memberEmail = memberEmail;
    }
}
