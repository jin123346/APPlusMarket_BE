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
    private String nickName;
    private Boolean isNegotiable;
    private Boolean isPossibleMeetYou;
    private String category;
    private int sequence;
    private String findProductId;
}
