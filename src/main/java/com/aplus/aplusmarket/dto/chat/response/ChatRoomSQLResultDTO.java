package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomSQLResultDTO {

    private int chatRoomId;
   private String productThumbnail;
    private String productName;
    private int price;
    private Boolean isNegotiable;
    private int productId;
}
