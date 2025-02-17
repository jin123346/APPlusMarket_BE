package com.aplus.aplusmarket.controller.chat;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatRoomCreateDTO;
import com.aplus.aplusmarket.entity.ChatRoom;
import com.aplus.aplusmarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    /** user_id로 채팅방 목록 조회
     * @param userId
     * @return ResponseDTO
     */
    @GetMapping("/chat-rooms")
    public ResponseDTO getChatRoomsByUserId(@RequestParam("userId") int userId) {
           return chatService.selectChatRoomsByUserId(userId);
    }

    /** id로 채팅방 상세 조회
     * @param chatRoomId
     * @return
     */
    @GetMapping("/chat-rooms/{id}")
    public ResponseDTO getChatRoomById(@PathVariable("id") int chatRoomId) {
        return chatService.selectChatRoomDetailsById(chatRoomId);
    }
    /** userId로 구독할 채팅방 id 조회
     * @param userId
     * @return List<int>
     */
    @GetMapping("/chat-rooms/id")
    public ResponseDTO getChatRoomIdsByUserId(@RequestParam("userId") int userId) {
        return chatService.selectChatRoomIdsByUserId(userId);
    }

    /** 채팅방 생성
     * @param chatRoom
     * @return
     */
    @PostMapping("/chat-rooms")
    public ResponseDTO insertChatRoom(@RequestBody ChatRoomCreateDTO chatRoom) {
        return chatService.insertChatRoom(chatRoom);
    }
}
