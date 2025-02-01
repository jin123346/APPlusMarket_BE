package com.aplus.aplusmarket.dto.auth.requset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String uid;
    private String password;
    private String deviceInfo;
}
