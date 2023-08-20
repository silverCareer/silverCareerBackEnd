package com.example.demo.src.payment.domain;

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

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "payment_name", nullable = false)
    private String paymentName;

    @Column(name = "product_idx", nullable = false)
    private Long productIdx;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @Builder
    public Payment(Long paymentIdx, LocalDate paymentDate, Long paymentAmount,
                   boolean paymentSuccess, String paymentType, String paymentName, Long productIdx, Member member) {
        this.paymentIdx = paymentIdx;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.paymentSuccess = paymentSuccess;
        this.paymentType = paymentType;
        this.paymentName = paymentName;
        this.productIdx = productIdx;
        this.member = member;
    }
}
