package com.example.demo.src.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @Column(name = "payment_idx")
    private Long paymentIdx;
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    @Column(name = "payment_amount")
    private Long paymentAmount;
    @Column(name = "payment_success")
    private int payment_success;

    //ManyToOne auth
}
