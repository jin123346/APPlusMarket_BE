package com.aplus.aplusmarket.dto.chat;

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
    private String nickname;
    private String profileImage;
}
