package com.example.demo.src.member.repository;

import com.example.demo.src.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "authority")
    Optional<Member> findOneWithAuthorityByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findByUsername(String username);
    @Query("select m from Member m where m.username=:memberEmail and m.activated=false")
    Optional<Member> findDeletedUserByMemberEmail(String memberEmail);
    List<Member> findMembersByCategory(String category);

    Optional<Member> findByName(String name);
}
