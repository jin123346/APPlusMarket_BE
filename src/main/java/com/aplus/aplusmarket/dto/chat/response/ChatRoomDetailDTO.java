package com.aplus.aplusmarket.dto.chat.response;

import com.aplus.aplusmarket.dto.chat.ProductCardDTO;
import com.aplus.aplusmarket.dto.chat.UserCardDTO;
import lombok.*;

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
    private List<ChatMessageResponseDTO> messages;


    }



