package com.example.demo.src.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @Column(name = "prod_idx")
    private Long productIdx;
    @Column(name = "p_name")
    private String productName;
    @Column(name = "descriptions")
    private String descriptions;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "p_image")
    private String productImage;
    @Column(name = "sales_count")
    private Long salesCount;
    @Column(name = "likes")
    private Long likes;

    //product - payment
    //product - auth
}
