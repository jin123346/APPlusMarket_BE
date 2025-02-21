package com.aplus.aplusmarket.dto.chat.response;

import com.aplus.aplusmarket.documents.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDTO {

    private String _id;
    // 저장된 후 messageId를 받아오기 위하여
    private int chatRoomId;
    private String content;
    private int senderId;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
// 저장된 후 createdAt을 사용하려면 select 한번 더 해야함
// 그래서 java 단에서 현재값 지정 후 데이터 삽입

    public ChatMessageResponseDTO toDTO(ChatMessage message){
        return ChatMessageResponseDTO.builder()
                ._id(message.get_id())
                .chatRoomId(message.getChatRoomId())
                .content(message.getContent())
                .senderId(message.getUserId())
                .isRead(message.getIsRead())
                .createdAt(message.getCreatedAt())
                .deletedAt(message.getDeletedAt()).build();
    }
}

