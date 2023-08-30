package com.example.demo.src.chat.controller;

import com.example.demo.src.chat.domain.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/sendMessage")
    public void sendMessage(Message message) {
        // message.getChatId() 로 채팅방 ID를 가져올 수 있습니다.
        simpMessagingTemplate.convertAndSend("/topic/messages/" + message.getReceiver(), message);
    }
}