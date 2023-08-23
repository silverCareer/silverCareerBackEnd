package com.example.demo.global.security;

public enum JwtCode {
    ACCESS, // Valid token
    EXPIRED, // Token expired

    DENIED // Token denied (invalid)
}
