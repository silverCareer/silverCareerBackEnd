package com.example.demo.src.chat.dto;

import lombok.*;

import java.util.Date;


@Data
public class ChatCreateReq {
    private String user1;
    private String user2;
    private Message newMessage;


    @Data
    public static class Message {
        private String sender;
        private String content;
        private Date timestamp;
    }
}