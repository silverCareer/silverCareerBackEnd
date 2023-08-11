package com.example.demo.src.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class ChatSaveRes {
    private String _id;
    private String user1;
    private String user2;
    private List<MessageRes> messages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageRes {
        private String sender;
        private String content;
        private Date timestamp;
    }

    // getter, setter ...
}