package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDTO {

    private int chatRoomId;
    private ProductCardDTO productCard;
    private List<UserCardDTO> participants;
    private List<ChatMessageDTO> messages; // TODO : Message - Pagination


        // ChatRoomSQLResultDTO List 를 ChatRoomDetailDTO 로 변환하는 메서드

    }



