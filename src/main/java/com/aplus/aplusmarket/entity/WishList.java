package com.aplus.aplusmarket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WishList {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
