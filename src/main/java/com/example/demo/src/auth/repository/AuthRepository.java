package com.example.demo.src.auth.repository;

import com.example.demo.src.auth.domain.Auth;
import com.example.demo.src.auth.domain.Authority;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<Auth> findOneWithAuthoritiesByUsername(String username);
    Optional<Auth> findByUsername(String username);
}
