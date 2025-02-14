package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCardResponseDTO {

    private int chatRoomId;
    private int unRead = 10;
    private String productThumbnail;
    private int productId;
    private int sellerId;// 채팅방의 제품 판매자

    private int userId;
    private String userNickname;
    private String userImage;

    private String recentMessage;
    private String messageCreatedAt;

}
