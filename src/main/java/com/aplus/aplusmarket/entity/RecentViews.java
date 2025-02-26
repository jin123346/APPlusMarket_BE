package com.aplus.aplusmarket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentViews {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime viewedAt;
}
