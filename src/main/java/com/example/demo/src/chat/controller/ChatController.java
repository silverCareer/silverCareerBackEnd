package com.example.demo.src.chat.controller;

import com.example.demo.src.chat.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/sendMessage")
    public void sendMessage(Message message, SimpMessageHeaderAccessor headerAccessor) {
        String destination = "/topic/messages/" + message.getReceiver();
        headerAccessor.getSessionAttributes().put("destination", destination);
        this.messagingTemplate.convertAndSend(destination, message);
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
}