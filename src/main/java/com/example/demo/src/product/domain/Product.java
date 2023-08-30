package com.example.demo.src.product.domain;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.review.domain.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "descriptions", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "product_images", columnDefinition = "TEXT", nullable = false)
    private String image;

    @Column(name = "sales_count", columnDefinition = "BIGINT")
    private Long saleCount;

    @Column(name = "likes", columnDefinition = "BIGINT")
    private Long likes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Product(Long productIdx, String productName, String category, String address, String description, Long price,
                   String image, Long saleCount, Long likes, Member member) {
        this.productIdx = productIdx;
        this.productName = productName;
        this.category = category;
        this.address = address;
        this.description = description;
        this.price = price;
        this.image = image;
        this.saleCount = saleCount;
        this.likes = 0L;
        this.member = member;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void increaseLikesCount() {
        this.likes += 1;
    }

    public void decreaseLikesCount() {
        this.likes -= 1;
    }
}