package com.aplus.aplusmarket.dto.chat.request;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageCreateDTO {

    private int ChatRoomId;
    private String content;
    private int SenderId;
}
