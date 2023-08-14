package com.example.demo.src.chat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "chat")
public class Chat {

    @Id
    private String _id;
    private String user1;
    private String user2;
    private List<Message> messages;

    @Data
    public static class Message {
        private String sender;
        private String content;
        private Date timestamp;
    }
}
