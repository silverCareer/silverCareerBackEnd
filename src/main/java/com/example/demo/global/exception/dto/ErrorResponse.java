package com.example.demo.global.exception.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int status,
        String code,
        String message
) {


}
