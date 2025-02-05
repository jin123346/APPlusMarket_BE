package com.aplus.aplusmarket.dto.chat.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Integer messageId;
    private Integer senderId;
    private String content;
    private String createdAt;
    private String deletedAt;
    private Boolean isRead;}


