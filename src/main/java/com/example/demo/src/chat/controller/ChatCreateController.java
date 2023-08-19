package com.example.demo.src.chat.controller;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.src.chat.dto.ChatCreateReq;
import com.example.demo.src.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ChatCreateController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat/create")
    public BaseResponse<String> createChatRoom(@RequestBody ChatCreateReq request) {
        try {
            chatRoomService.createChatRoom(request);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch(Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }
}