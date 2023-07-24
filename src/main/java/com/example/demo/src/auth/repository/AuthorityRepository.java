package com.example.demo.src.auth.repository;

import com.example.demo.src.auth.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
