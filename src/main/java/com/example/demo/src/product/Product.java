package com.example.demo.src.product;

import com.example.demo.src.auth.domain.Authority;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "prod_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodIdx;

    @Column(name = "p_name")
    private String productName;

    @Column(name = "descriptions")
    private String description;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "p_image")
    private String image;

    @Column(name = "sales_count")
    private Long saleCount;

    @Column(name = "likes")
    private Long likes;

    public Product() {
    }


    // 생성자, getter, setter 등이 있을 수 있습니다.
    // ...

    // Getter와 Setter 메서드들을 작성합니다.
    // ...
    @Builder
    public Product(Long prodIdx,String productName, String description, String categoryName,
                   String image, Long saleCount, Long likes) {
        this.prodIdx = prodIdx;
        this.productName = productName;
        this.description = description;
        this.categoryName = categoryName;
        this.image = image;
        this.saleCount = saleCount;
        this.likes = likes;
    }

}