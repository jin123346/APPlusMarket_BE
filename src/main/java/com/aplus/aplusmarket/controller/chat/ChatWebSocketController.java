package com.aplus.aplusmarket.controller.chat;

import com.aplus.aplusmarket.documents.ChatMessage;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageDTO;
import com.aplus.aplusmarket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;


/*
 * packageName    : com/aplus/aplusmarket/controller/chat/ChatWebSocketController.java
 * fileName       : ChatWebSocketController.java
 * author         : 황수빈
 * date           : 2024/02/05
 * description    : Chat WebSocketController
 *
 * =============================================================
 * DATE           AUTHOR             NOTE
 * -------------------------------------------------------------
 * 2025.02.05     황수빈     메시지 생성 후 방송하는 메서드 추가
 *
 */



@Log4j2
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    // 클라이언트가 /pub/chat/message로 전송하면 이 메서드가 처리됨

    /** 메시지 저장 후 웹소켓 방송
     * @param chatMessageDTO
     * @return ResponseDTO
     */
    @Transactional
    @MessageMapping("/chat/message")
    public ResponseDTO sendMessage(ChatMessageDTO chatMessageDTO) {

        log.info("💻 메시지 저장 전 입력 값" +  chatMessageDTO);
        ChatMessage result = chatMessageService.insertChatMessage(chatMessageDTO);

        chatMessageDTO.setMessageId(result.get_id());
        chatMessageDTO.setCreatedAt(result.getCreatedAt());

        messagingTemplate.convertAndSend("/sub/chatroom/" + result.getChatRoomId(), chatMessageDTO);
        log.info("💻 웹소켓 메시지 저장 후 전송 "+ result);
        return ResponseDTO.of("success", 4000, "Message broadcasted successfully");
    }


}

