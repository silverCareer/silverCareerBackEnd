package com.example.demo.src.chat.controller;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.src.chat.domain.Chat;
import com.example.demo.src.chat.dto.ChatSaveRes;
import com.example.demo.src.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatRoomService chatRoomService;


    @GetMapping("/chat")
    public BaseResponse<List<ChatSaveRes>> findBySenderId(@RequestParam String senderId) {
        try {
            List<ChatSaveRes> chatRes = chatRoomService.findBySenderId(senderId);
            return new BaseResponse<>(chatRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch(Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR); // 예시
        }
    }


}