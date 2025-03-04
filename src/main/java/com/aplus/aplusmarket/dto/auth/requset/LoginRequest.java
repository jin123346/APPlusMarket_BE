package com.aplus.aplusmarket.dto.auth.requset;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String uid;
    private String password;
    private String deviceInfo;
    private String tempUserId;
}
