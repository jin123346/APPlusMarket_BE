package com.aplus.aplusmarket.controller;


import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.response.ChatRoomCardResponseDTO;
import com.aplus.aplusmarket.entity.ChatMessage;
import com.aplus.aplusmarket.entity.ChatRoom;
import com.aplus.aplusmarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/*
*  2025.01.31 황수빈 - 더미데이터 추가 후 조회
*  2025.02.03 황수빈 - 채팅방 상세정보 가져오기 시도중
*
* */
@RestController
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    final ChatService chatService;

    // 유저 아이디로 채팅방 목록 조회
    @GetMapping("/chat-rooms")
    public ResponseDTO getChatRoomsByUserId(@RequestParam("userId") int userId) {

           return chatService.selectChatRoomsByUserId(userId);
    }

    // TODO - 컨트롤러 완성
    @GetMapping("/chat-rooms/{id}")
    public ResponseDTO getChatRoomById(@PathVariable("id") int chatRoomId) {
        return chatService.selectChatRoomDetailsById(1);
    }
}
