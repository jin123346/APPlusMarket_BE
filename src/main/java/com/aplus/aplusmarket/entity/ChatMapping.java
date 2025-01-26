package com.aplus.aplusmarket.entity;
import lombok.*;

import java.time.LocalDateTime;

@Getter

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMapping {
    private Integer id;
    private Integer userId;
    private LocalDateTime deletedAt;
    private Integer chatRoomId;



}