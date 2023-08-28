package com.example.demo.src.likes.repository;

import com.example.demo.src.likes.domain.Like;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByProductAndMember(Product product, Member member);

    Optional<Like> findByProductAndMember(Product product, Member member);
}
