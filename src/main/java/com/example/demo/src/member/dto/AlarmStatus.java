package com.example.demo.src.member.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AlarmStatus {
    private Boolean status;
    private String message;
}
