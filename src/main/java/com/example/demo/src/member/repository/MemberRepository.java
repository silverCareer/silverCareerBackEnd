package com.example.demo.src.member.repository;

import com.example.demo.src.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "authority")
    Optional<Member> findOneWithAuthorityByUsername(String username);
}
