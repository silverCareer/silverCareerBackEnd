package com.example.demo.src.kakao;

import com.example.demo.src.auth.domain.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Auth, Long> {

}
