package com.aplus.aplusmarket.entity;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ChatMessage {
    private Integer id;
    private Integer userId;
    private Integer chatRoomId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private Boolean isRead;}