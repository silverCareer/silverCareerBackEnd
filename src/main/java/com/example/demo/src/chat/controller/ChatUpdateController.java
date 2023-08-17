package com.example.demo.src.chat.controller;

import com.example.demo.global.exception.BaseException;
import com.example.demo.global.exception.BaseResponse;
import com.example.demo.global.exception.BaseResponseStatus;
import com.example.demo.src.chat.dto.ChatSaveReq;
import com.example.demo.src.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ChatUpdateController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat/update")
    public BaseResponse<String> updateChat(@RequestBody ChatSaveReq request) {
        try {
            chatRoomService.updateChat(request);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        } catch(Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }
}