package com.aplus.aplusmarket.dto.chat.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageCreateDTO {

    private int chatMessageId;
    private int chatRoomId;
    private String content;
    private int senderId;
    private LocalDateTime createdAt;
    // 저장된 후 createdAt을 사용하려면 select 한번 더 해야함
    // 그래서 java 단에서 now()로 현재 시간 set 후 데이터 삽입
}

