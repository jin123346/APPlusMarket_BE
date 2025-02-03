package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

import java.util.List;

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
    private int userId;
    private String userName;
    private String profileImage;
    private int chatMessageId;
    private String content;
    private String createAt;



}
