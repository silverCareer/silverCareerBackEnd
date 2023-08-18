package com.example.demo.src.likes.repository;

import com.example.demo.src.likes.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByProductIdxAndMemberEmail(Long productIdx ,String username);
    Optional<Like> findByProductIdxAndMemberEmail(Long productIdx, String username);
}
