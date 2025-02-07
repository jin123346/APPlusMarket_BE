package com.aplus.aplusmarket.dto.auth;

import com.aplus.aplusmarket.entity.User;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String uid;
    private String password;
    private String email;
    private String hp;
    private String profileImg;
    private String nickName;
    private String name;
    private String  status; // enum 변경할 예정
    private long payBalance;
    private LocalDateTime birthday;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private int currentRate;
    private int reportCount;
    private int sellCount;
    private String role;   // USER, ADMIN 두개

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
                .nickname(nickName)
                .build();


    }

    public static UserDTO loginUser(User user){
        return UserDTO.builder()
                .id(user.getId())
                .uid(user.getUid())
                .hp(user.getHp())
                .email(user.getEmail())
                .nickName(user.getNickname())
                .name(user.getName())
                .profileImg(user.getProfileImg())
                .status(user.getStatus())
                .birthday(user.getBirthday())
                .reportCount(user.getReportCount())
                .currentRate(user.getCurrentRate())
                .payBalance(user.getPayBalance())
                .sellCount(user.getSellCount())
                .build();
    }

}
