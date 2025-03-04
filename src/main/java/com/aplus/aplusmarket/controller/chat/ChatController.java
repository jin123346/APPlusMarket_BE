package com.aplus.aplusmarket.controller.chat;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatRoomCreateDTO;
import com.aplus.aplusmarket.service.ChatMessageService;
import com.aplus.aplusmarket.service.ChatService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/*
 * packageName    : com/aplus/aplusmarket/controller/chat/ChatController.java
 * fileName       : ChatController.java
 * author         : 황수빈
 * date           : 2024/01/31
 * description    : Chat 기능 Controller
 *
 * =============================================================
 * DATE           AUTHOR             NOTE
 * -------------------------------------------------------------
 * 2025.01.31     황수빈     채팅방 목록 조회
 * 2025.02.03     황수빈     채팅방 상세 정보 조회
 * 2025.02.11     황수빈     구독할 채팅방 id 조회
 * 2025.02.18     황수빈     MySQL , Mongo 성능 비교 -> Mongo 로 설정
 */



@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
@Log4j2
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;

    /** 채팅방 목록 조회
     * @param userId
     * @return List<ChatRoomCardResponseDTO>
     */
    @GetMapping()
    public ResponseDTO getChatRoomsByUid(@RequestParam("userId") int userId) {
        return chatService.selectChatRoomsByUid(userId);
    }

    /** 채팅방 상세 조회
     * @param chatRoomId
     * @return ChatRoomDetailDTO
     */
    @GetMapping("/{id}")
    public ResponseDTO getChatInfoById(@PathVariable("id") int chatRoomId) {
        return chatService.selectChatRoomInfo(chatRoomId);
    }

    /** 구독할 채팅방 id 조회
     * @param userId
     * @return List<int>
     */
    @GetMapping("/id")
    public ResponseDTO getChatRoomIdsByUserId(@RequestParam("userId") int userId) {
        return chatService.selectChatRoomIdsByUserId(userId);
    }

    /** 채팅방 생성
     * @param chatRoom
     * @return chatRoomId
     */
    @PostMapping()
    public ResponseDTO insertChatRoom(@RequestBody ChatRoomCreateDTO chatRoom) {
        return chatService.insertChatRoom(chatRoom);
    }

    @PutMapping("/{chatRoomId}/messages/{messageId}")
    public ResponseDTO updateAppointment(@PathParam("chatRoomId") Integer chatRoomId,
                                         @PathParam("messageId") String messageId,
                                        @RequestBody ChatMessageDTO chatMessage){
        return chatService.updateAppointment(chatMessage);
    }
    // ✅ lastCreatedAt 기준으로 이전 메시지 가져오기
    @GetMapping("/{chatRoomId}/messages")
    public ResponseDTO getPreviousMessagesByTime(
            @PathVariable("chatRoomId") int chatRoomId,
            @RequestParam("beforeCreatedAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeCreatedAt) {

        log.info("이전 메시지 로딩: chatRoomId={}, beforeCreatedAt={}", chatRoomId, beforeCreatedAt);

        return chatMessageService.getPreviousMessagesByTime(chatRoomId, beforeCreatedAt);
    }
}
