package com.aplus.aplusmarket.documents;


import com.aplus.aplusmarket.dto.chat.response.ChatMessageResponseDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatMessage")

/*
 * 2025.02.18 - 황수빈 : message를 기존 MySQL에서 mongo로 변경
 */

public class ChatMessage {

    @Id
    private String _id;
    private int userId;
    private int chatRoomId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean isRead;


    public ChatMessageResponseDTO toDTO(ChatMessage chatMessage){
        return ChatMessageResponseDTO.builder()
                ._id(chatMessage.get_id())
                .senderId(chatMessage.getUserId())
                .chatRoomId(chatMessage.getChatRoomId())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .deletedAt(chatMessage.getDeletedAt())
                .isRead(chatMessage.getIsRead())
                .build();
    }
}
