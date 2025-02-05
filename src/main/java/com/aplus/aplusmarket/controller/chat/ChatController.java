package com.aplus.aplusmarket.controller.chat;


import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageCreateDTO;
import com.aplus.aplusmarket.entity.ChatMessage;
import com.aplus.aplusmarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

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

    // 채팅방 아이디로 채팅방 상세 조회
    @GetMapping("/chat-rooms/{id}")
    public ResponseDTO getChatRoomById(@PathVariable("id") int chatRoomId) {
        return chatService.selectChatRoomDetailsById(1);
    }


//    @PostMapping("/chat/message")
//    public ResponseDTO sendMessage(@RequestBody ChatMessageCreateDTO chatMessage) {
//
//        return chatService.insertChatMessage(chatMessage);
//
//    }

}
