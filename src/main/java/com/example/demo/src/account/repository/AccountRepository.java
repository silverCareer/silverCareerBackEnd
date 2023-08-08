package com.example.demo.src.account.repository;

import com.example.demo.src.account.domain.Account;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByMember_Username(String email);
}
