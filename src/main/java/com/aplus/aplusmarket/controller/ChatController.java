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
*
* */
@RestController
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    final ChatService chatService;

    @GetMapping("/chat-rooms")
    public ResponseDTO selectChatRoomByUserId(@RequestParam int userId) {
        try {
            log.info("채팅방 조회 요청 userId : {}", userId);
            List<ChatRoomCardResponseDTO> chatRooms = chatService.selectAllChatRoomCards(userId);

            if (chatRooms == null || chatRooms.isEmpty()) {
                return ResponseDTO.builder()
                        .code(4000)
                        .message("채팅방이 존재하지 않습니다.")
                        .build();
            }

            return new DataResponseDTO<>(chatRooms, 4000, "채팅방 조회 성공");

        } catch (Exception e) {
            log.error(e);
            return ErrorResponseDTO.of(4001, "채팅방 조회 실패 : " + e.getMessage());
        }
    }


}
