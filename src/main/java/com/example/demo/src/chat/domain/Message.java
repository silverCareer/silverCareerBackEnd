package com.example.demo.src.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String content;
    private String sender;
    private String type;
    private String receiver;
}