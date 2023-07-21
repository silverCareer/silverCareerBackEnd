package com.example.demo.src.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "authority")
@Getter
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;

    @Builder
    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
