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

    private String productThumbnail;

    private int productId;
    private Boolean isSeller; // 채팅방의 제품 판매자
    private String userImage;
    private String userName;
    private String recentMessage;
    private String messageCreatedAt;

}
