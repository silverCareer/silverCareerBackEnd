package com.example.demo.src.review.repository;

import com.example.demo.src.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.productIdx = :productIdx AND r.member.username = :memberEmail")
    long checkReviewer(@Param("productIdx") Long productIdx, @Param("memberEmail") String memberEmail);
}
