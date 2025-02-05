package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDTO {

    private int messageId;
    private int chatRoomId;
    private String content;
    private int senderId;
    private String createdAt;

}
