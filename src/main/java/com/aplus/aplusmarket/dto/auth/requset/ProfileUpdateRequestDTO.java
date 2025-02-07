package com.aplus.aplusmarket.dto.auth.requset;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequestDTO {
    private long id;
    private String profileImg;
}
