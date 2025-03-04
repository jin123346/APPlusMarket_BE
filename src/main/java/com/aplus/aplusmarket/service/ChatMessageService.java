package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.documents.ChatMessage;
import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageDTO;
import com.aplus.aplusmarket.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;
    /** 메시지 삽입
     * @param messageDTO
     * @return ChatMessage
     */
    public ChatMessage insertChatMessage(ChatMessageDTO messageDTO) {

       ChatMessage message =  messageDTO.toDocument(messageDTO);
       message.setCreatedAt(LocalDateTime.now());
       return chatMessageRepository.save(message);
    }

    /** 최근 메시지 30개 조회
     * @param chatRoomId
     * @return List<ChatMessageResponseDTO>
     */
    public List<ChatMessageDTO> getChatMessages(int chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findTop30ByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
        return messages.stream()
                .map(message -> message.toDTO(message))
                .collect(Collectors.toList());
    }

    /** 최근 메시지 조회
     * @param chatRoomId
     * @return ChatMessage
     */
    public ChatMessage getRecentMessageByChatRoomId(int chatRoomId) {
        return chatMessageRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoomId)
                .orElseThrow(() -> new RuntimeException("RecentMessage Not Found"));
    }

    /** 메시지 수정
     * @param messageDTO
     * @return ChatMessage
     */
    public ChatMessage updateAppointment(ChatMessageDTO messageDTO) {

        ChatMessage message =  messageDTO.toDocument(messageDTO);
        message.setCreatedAt(LocalDateTime.now());
        return chatMessageRepository.save(message);
    }

    /**
     * 이전 메시지 조회
     * @param chatRoomId
     * @param lastCreatedAt
     * @return
     */
    public ResponseDTO getPreviousMessagesByTime(int chatRoomId, LocalDateTime lastCreatedAt) {
        List<ChatMessage> messages = chatMessageRepository
                .findTop30ByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(chatRoomId, lastCreatedAt);
        List<ChatMessageDTO> result = messages.stream()
                .map(message -> message.toDTO(message))
                .collect(Collectors.toList());
        // DTO로 변환하여 반환
        return DataResponseDTO.success(result,4000);
    }

    /**
     * 특정 채팅방에서 특정 사용자의 해당 시점 이전의 메시지를 읽음 처리
     * @param chatRoomId
     * @param userId
     * @param timestamp
     * @return ResponseDTO
     */
    /**
     /**
     * ✅ 특정 채팅방에서 userId가 아닌 사용자가 보낸, 특정 시점 이전의 메시지를 읽음 처리 (isRead = true)
     * @param chatRoomId 채팅방 ID
     * @param userId 현재 사용자의 ID
     * @param timestamp 사용자가 채팅방에 들어간 시점
     * @return 업데이트된 메시지 개수
     */
    public ResponseDTO markMessagesAsRead(int chatRoomId, int userId, LocalDateTime timestamp) {
        try {
            // ✅ Query 생성 (빌더 패턴 사용)
            Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId)
                    .and("userId").ne(userId)  // 내가 보낸 메시지는 제외
                    .and("createdAt").lte(timestamp)); // timestamp 이전 메시지

            // ✅ Update 문서 설정
            Update update = new Update().set("isRead", true);

            // ✅ MongoDB에서 다중 업데이트 실행
            var updateResult = mongoTemplate.updateMulti(query, update, ChatMessage.class);

            log.info("읽음 처리 갯수"+updateResult);
            return ResponseDTO.of("success", 4000, "업데이트 완료");
            // 업데이트된 메시지 개수 반환
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDTO.of("fail", 4004, "에러 발생"); // 실패 시 0 반환
        }
    }
}



