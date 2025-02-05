package com.aplus.aplusmarket.dto.auth.requset;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@Data
public class MyInfoUpdateRequest {

    Long  id;
    String nickName;
    String hp;
    String email;
}
