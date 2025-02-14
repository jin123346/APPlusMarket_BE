package com.aplus.aplusmarket.dto.chat.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCreateDTO {

    private int chatRoomId;
    // 저장된 후 chatRoomId 를 받아오기 위하여
    private int sellerId;
    private int productId;
    private int userId;

    private LocalDateTime createdAt;

}
