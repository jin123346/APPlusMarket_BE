package com.aplus.aplusmarket.dto.chat.request;

import com.aplus.aplusmarket.documents.ChatMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.json.JSONPropertyName;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {

    @JsonProperty("messageId")
    private String messageId;
    private int chatRoomId;

    @JsonProperty("userId")
    private int userId;
    private String content;

    // appointment 용 컬럼
    private String date;
    private String time;
    private String location;
    private String locationDescription;
    private int reminderBefore;

    private LocalDateTime createdAt;

    private Boolean isFirst;
    // 저장된 후 createdAt을 사용하려면 select 한번 더 해야함
    // 그래서 java 단에서 now()로 현재 시간 set 후 데이터 삽입

    public ChatMessage toDocument(ChatMessageDTO dto){
        return ChatMessage.builder()
                ._id(dto.getMessageId())
                .chatRoomId(dto.getChatRoomId())
                .userId(dto.getUserId())
                .content(dto.getContent())
                .date(dto.getDate())
                .time(dto.getTime())
                .location(dto.getLocation())
                .reminderBefore(dto.getReminderBefore())
                .locationDescription(dto.getLocationDescription())
                .createdAt(dto.getCreatedAt())
                .build();

    }

}

