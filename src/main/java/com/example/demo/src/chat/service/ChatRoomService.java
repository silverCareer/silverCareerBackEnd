package com.example.demo.src.chat.service;


import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.src.chat.domain.Chat;
import com.example.demo.src.chat.dto.ChatSaveRes;
import com.example.demo.src.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRepository chatRepository;

    public List<ChatSaveRes> findBySenderId(String senderId) throws BaseException {
        List<Chat> chats = chatRepository.findByMessagesSender(senderId);

        if(chats == null || chats.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NOT_FOUND_CHAT);
        }

        List<ChatSaveRes> chatResponses = new ArrayList<>();
        for(Chat chat : chats) {
            ChatSaveRes chatRes = new ChatSaveRes();
            chatRes.set_id(chat.get_id());
            chatRes.setUser1(chat.getUser1());
            chatRes.setUser2(chat.getUser2());

            List<ChatSaveRes.MessageRes> messageResponses = new ArrayList<>();
            for(Chat.Message message : chat.getMessages()) {
                ChatSaveRes.MessageRes messageRes = new ChatSaveRes.MessageRes();
                messageRes.setSender(message.getSender());
                messageRes.setContent(message.getContent());
                messageRes.setTimestamp(message.getTimestamp());
                messageResponses.add(messageRes);
            }
            chatRes.setMessages(messageResponses);
            chatResponses.add(chatRes);
        }

        return chatResponses;
    }

}
