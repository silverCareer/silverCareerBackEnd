package com.example.demo.src.payment.shared.domain;

import com.example.demo.src.member.domain.Member;
import com.example.demo.src.product.domain.Product;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_idx", nullable = false)
    private Long paymentIdx;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "payment_amount", nullable = false)
    private Long paymentAmount;

    @Column(name = "payment_success", nullable = false)
    private boolean paymentSuccess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_idx", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Builder
    public Payment(Long paymentIdx, LocalDate paymentDate, Long paymentAmount,
                   boolean paymentSuccess, Product product, Member member) {
        this.paymentIdx = paymentIdx;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.paymentSuccess = paymentSuccess;
        this.product = product;
        this.member = member;
    }
}
