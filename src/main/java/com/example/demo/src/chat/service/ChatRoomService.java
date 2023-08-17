package com.example.demo.src.chat.service;


import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.src.chat.domain.Chat;
import com.example.demo.src.chat.dto.ChatCreateReq;
import com.example.demo.src.chat.dto.ChatSaveReq;
import com.example.demo.src.chat.dto.ChatSaveRes;
import com.example.demo.src.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.src.chat.dto.ChatSaveReq;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRepository chatRepository;
    private final MongoTemplate mongoTemplate;

    public List<ChatSaveRes> findByUser(String username) throws BaseException {
        List<Chat> chats = chatRepository.findByUser1OrUser2(username, username);

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

    public List<ChatSaveRes> findById(String chatId) throws BaseException {
        List<Chat> chats = chatRepository.findBy_id(chatId);

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

    public void updateChat(ChatSaveReq request) throws BaseException {
        Query query = new Query(Criteria.where("_id").is(request.get_id()));
        Update update = new Update().push("messages", request.getMessage());

        mongoTemplate.findAndModify(query, update, Chat.class);
    }

    public Chat createChatRoom(ChatCreateReq chatCreateReq) throws BaseException {
        Chat newChatRoom = new Chat();
        newChatRoom.setUser1(chatCreateReq.getUser1());
        newChatRoom.setUser2(chatCreateReq.getUser2());

        List<Chat.Message> initialMessage = new ArrayList<>();

        // ChatCreateReq의 Message 객체를 가져옵니다.
        ChatCreateReq.Message createReqMessage = chatCreateReq.getNewMessage();

        // Chat의 Message 객체를 생성하고 값을 설정합니다.
        Chat.Message chatMessage = new Chat.Message();
        chatMessage.setSender(createReqMessage.getSender());
        chatMessage.setContent(createReqMessage.getContent());
        chatMessage.setTimestamp(createReqMessage.getTimestamp());

        // 생성된 Chat.Message 객체를 리스트에 추가합니다.
        initialMessage.add(chatMessage);

        newChatRoom.setMessages(initialMessage);

        return chatRepository.save(newChatRoom);
    }

}
