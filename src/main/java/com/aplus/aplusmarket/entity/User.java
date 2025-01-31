package com.aplus.aplusmarket.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private long id;
    private String uid;
    private String password;
    private String email;
    private String hp;
    private String profileImg;
    private String nickname;
    private String name;
    private String status; // enum 변경할 예정
    private long payBalance;

    private LocalDateTime birthday;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private int currentRate;
    private int reportCount;
    private int sellCount;
    private String role;   // USER, ADMIN 두개
}
