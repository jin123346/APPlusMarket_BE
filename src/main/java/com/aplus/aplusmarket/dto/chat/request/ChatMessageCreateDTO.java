package com.aplus.aplusmarket.dto.chat.request;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageCreateDTO {
    private int messageId;
    private int chatRoomId;
    private String content;
    private int senderId;
    private LocalDateTime  createdAt;

}
