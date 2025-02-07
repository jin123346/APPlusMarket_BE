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
    // 저장된 후 messageId를 받아오기 위하여
    private int chatRoomId;
    private String content;
    private int senderId;
    private String createdAt;
    // 저장된 후 createdAt을 사용하려면 select 한번 더 해야함
    // 그래서 java 단에서 현재값 지정 후 데이터 삽입

}
