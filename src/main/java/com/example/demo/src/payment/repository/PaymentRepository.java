package com.example.demo.src.payment.repository;

import com.example.demo.src.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<List<Payment>> findPaymentsByMember_Username(String email);
    Payment findPaymentByMember_UsernameAndProductIdx(String email, Long productIdx);
}
