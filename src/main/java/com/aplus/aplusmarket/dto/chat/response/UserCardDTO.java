package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCardDTO {

    private int userId;
    private String userName;
    private String profileImage;
}
