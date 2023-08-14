package com.example.demo.src.payment.shared.repository;

import com.example.demo.src.account.domain.Account;
import com.example.demo.src.payment.shared.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<List<Payment>> findPaymentsByMember_Username(String email);
}
