package com.aplus.aplusmarket.dto.auth.requset;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDTO {
    private Long id;
    private String uid;
    private String nickName;
}
