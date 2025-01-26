package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCardResponseDTO {

    int chatRoomId;
    String productThumbnail;
    int productId;
    Boolean isSeller; // 채팅방의 제품 판매자
    String UserImage;
    String UserName;
    String RecentMessage;
    String MessageCreatedAt;

}
