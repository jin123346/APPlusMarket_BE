package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.documents.ChatMessage;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageCreateDTO;
import com.aplus.aplusmarket.dto.chat.response.ChatMessageResponseDTO;
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
    public ChatMessage insertChatMessage(ChatMessageCreateDTO messageDTO) {

        ChatMessage message =
                ChatMessage.builder()
                .chatRoomId(messageDTO.getChatRoomId())
                .userId(messageDTO.getSenderId())
                .content(messageDTO.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(message);
    }

    /** 최근 메시지 30개 조회
     * @param chatRoomId
     * @return List<ChatMessageResponseDTO>
     */
    public List<ChatMessageResponseDTO> getChatMessages(int chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findTop30ByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
        return messages.stream()
                .map(message -> new ChatMessageResponseDTO().toDTO(message))
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

}



