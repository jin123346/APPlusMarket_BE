package com.aplus.aplusmarket.documents;


import com.aplus.aplusmarket.dto.chat.request.ChatMessageDTO;
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

    // appointment 용 컬럼
    private String date;
    private String time;
    private String location;
    private String locationDescription;
    private int reminderBefore;



    public ChatMessageDTO toDTO(ChatMessage chatMessage){
        return ChatMessageDTO.builder()
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(chatMessage.getUserId())
                .content(chatMessage.getContent())
                .date(chatMessage.getDate())
                .time(chatMessage.getTime())
                .location(chatMessage.getLocation())
                .reminderBefore(chatMessage.getReminderBefore())
                .locationDescription(chatMessage.getLocationDescription())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
