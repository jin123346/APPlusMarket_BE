package com.aplus.aplusmarket.dto.auth.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MyInfoUser {
    long id;
    String uid;
    String nickName;
    String name;
    LocalDateTime birthDay;
    String hp;
    String email;
}
