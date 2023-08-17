package com.example.demo.src.chat.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ChatSaveReq {
    private String _id;  // Chat room ID to find and update the specific chat room.
    private MessageReq message;

    @Data
    public static class MessageReq {
        private String sender;
        private String content;
        private Date timestamp;
    }
}