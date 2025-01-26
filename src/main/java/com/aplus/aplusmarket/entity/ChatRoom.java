package com.aplus.aplusmarket.entity;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    private Integer id;
    private Integer productId;
    private LocalDateTime createdAt;
    private Integer sellerId;
}