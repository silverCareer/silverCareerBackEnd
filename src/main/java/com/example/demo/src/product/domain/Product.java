package com.example.demo.src.product;

import com.example.demo.src.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productIdx;

    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @Column(name = "category", length = 20, nullable = false)
    private String category;

    @Column(name = "descriptions", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "product_images", columnDefinition = "JSON")
    private String image;

    @Column(name = "sales_count", columnDefinition = "BIGINT")
    private Long saleCount;

    @Column(name = "likes")
    private Long likes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Builder
    public Product(Long productIdx, String productName, String category, String description, Long price,
                   String image, Long saleCount, Long likes, Member member) {
        this.productIdx = productIdx;
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.price = price;
        this.image = image;
        this.saleCount = saleCount;
        this.likes = likes;
        this.member = member;
    }
}