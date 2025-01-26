package com.aplus.aplusmarket.controller;


import com.aplus.aplusmarket.dto.chat.response.ChatRoomCardResponseDTO;
import com.aplus.aplusmarket.entity.ChatMessage;
import com.aplus.aplusmarket.entity.ChatRoom;
import com.aplus.aplusmarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    final ChatService chatService;

    @GetMapping("/chatRoom")
    public ResponseEntity<List<ChatRoomCardResponseDTO>> check() {
        List<ChatRoomCardResponseDTO> chatRooms = chatService.selectAllChatRoomCards();

    return ResponseEntity.ok().body(chatRooms);
    }


}
