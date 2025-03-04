package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.documents.ChatMessage;
import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageDTO;
import com.aplus.aplusmarket.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

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
}



