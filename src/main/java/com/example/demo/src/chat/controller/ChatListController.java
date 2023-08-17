package com.example.demo.src.chat.controller;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.src.chat.dto.ChatSaveRes;
import com.example.demo.src.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ChatListController {

    private final ChatRoomService chatRoomService;


    @GetMapping("/chat")
    public BaseResponse<List<ChatSaveRes>> findByUser(@RequestParam String username) {
        try {
            List<ChatSaveRes> chatRes = chatRoomService.findByUser(username);
            return new BaseResponse<>(chatRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch(Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR); // 예시
        }
    }

    @GetMapping("/latestChat")
    public BaseResponse<List<ChatSaveRes>> findById(@RequestParam String chatId) {
        try {
            List<ChatSaveRes> chatRes = chatRoomService.findById(chatId);
//            for (ChatSaveRes chat : chatRes) {
//                System.out.println(chat.getMessages());
//            }

            return new BaseResponse<>(chatRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch(Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR); // 예시
        }
    }
}