package com.example.demo.src.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @Column(name = "review_idx")
    private Long reviewIdx;
    @Column(name = "r_content")
    private String reviewContent;
    @Column(name = "r_images")
    private String reviewImages;

    //authIdx, prodductIdx랑 참조
}
