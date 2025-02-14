package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    private int chatMessageId;
    // 저장된 후 messageId를 받아오기 위하여
    private int chatRoomId;
    private String content;
    private int senderId;
    private LocalDateTime createdAt;
// 저장된 후 createdAt을 사용하려면 select 한번 더 해야함
// 그래서 java 단에서 현재값 지정 후 데이터 삽입
}

