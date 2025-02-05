package com.aplus.aplusmarket.controller.chat;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageCreateDTO;
import com.aplus.aplusmarket.dto.chat.response.ChatMessageResponseDTO;
import com.aplus.aplusmarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
@Log4j2
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 클라이언트가 /pub/chat/message로 전송하면 이 메서드가 처리됨

    @Transactional
    @MessageMapping("/chat/message")
    public ResponseDTO sendMessage(ChatMessageCreateDTO chatMessage) {
        ChatMessageCreateDTO resultDTO = chatService.insertMessage(chatMessage);
        log.info("resultDTO "+resultDTO);
        messagingTemplate.convertAndSend("/sub/chatroom/" + resultDTO.getChatRoomId(), resultDTO);
        return ResponseDTO.of("success", 4000, "Message broadcasted successfully");
    }
}

