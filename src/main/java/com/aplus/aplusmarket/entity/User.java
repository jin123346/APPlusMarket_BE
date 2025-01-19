package com.aplus.aplusmarket.entity;


import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String uid;
    private String password;
    private String email;
    private String profile_img;
    private String nickname;
    private String  status; // enum 변경할 예정
    private LocalDateTime birthday;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;
    private int current_rate;
    private int report_count;
    private int sell_count;

}
