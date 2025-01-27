package com.aplus.aplusmarket.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String title;
    private String productImages; // JSON 문자열로 처리
    private String productName;
    private String content;
    private String registerLocation;
    private String registerIp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reloadAt;
    private Integer price;
    private String status;
    private LocalDateTime deletedAt;
    private Long sellerId;
    private Boolean isNegotiable;
    private Boolean isPossibleMeetYou;
    private String category;
}
