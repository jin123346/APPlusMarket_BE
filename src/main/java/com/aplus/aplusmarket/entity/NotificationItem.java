package com.aplus.aplusmarket.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationItem {
    private Long id;
    private Long productId;
    private Long userId;
    private String eventType;
    private String message;
    private boolean isRead;
    private boolean isDeleted;
    private String timestamp;


}
