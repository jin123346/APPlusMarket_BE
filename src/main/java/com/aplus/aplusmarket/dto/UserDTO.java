package com.aplus.aplusmarket.dto;

import com.aplus.aplusmarket.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String uid;
    private String password;
    private String email;
    private String hp;
    private String profileImg;
    private String nickname;
    private String name;
    private String  status="ACTIVE"; // enum 변경할 예정
    private long payBalance=0;
    private LocalDateTime birthday;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private int currentRate=0;
    private int reportCount=0;
    private int sellCount=0;
    private String role="USER";   // USER, ADMIN 두개


    public User register() {


        return User.builder()
                .uid(uid)
                .password(password)
                .email(email)
                .hp(hp)
                .birthday(birthday)
                .name(name)
                .status(this.status)
                .payBalance(this.payBalance)
                .currentRate(this.currentRate)
                .sellCount(this.sellCount)
                .role(this.role)
                .reportCount(0)
                .currentRate(0)
                .createdAt(createdAt)
                .nickname(nickname)
                .build();


    }

}
