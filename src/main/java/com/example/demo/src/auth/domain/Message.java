package com.example.demo.src.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
@Table(name = "message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @Column(name = "message_idx")
    private Long meesageIdx;
    @Column(name = "m_content")
    private String messageContent;
    @Column(name = "sned_time")
    private LocalDate sendTime;

    //chatIdx랑 OneToOne
    //authIdx랑
}
