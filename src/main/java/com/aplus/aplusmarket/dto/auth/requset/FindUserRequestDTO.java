package com.aplus.aplusmarket.dto.auth.requset;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindUserRequestDTO {
    private long id;
    private String email;
    private String name;
    private String uid;
    private String password;
}
