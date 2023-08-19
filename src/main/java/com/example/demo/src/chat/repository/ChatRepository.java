package com.example.demo.src.chat.repository;

import com.example.demo.src.chat.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {

    List<Chat> findByUser1OrUser2(String username1, String username2);
    List<Chat> findBy_id(String chatId);
}