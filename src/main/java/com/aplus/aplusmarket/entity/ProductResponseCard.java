package com.aplus.aplusmarket.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseCard {
    private Long id;
    private String title;
    private String productImage;
    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reloadAt;
    private Integer price;
    private String status;
    private Boolean isNegotiable;
    private Boolean isPossibleMeetYou;
    private String category;
    private Long sellerId;
    private String registerLocation;
    private String uuidName;
    private String brand;
    private Long buyerId;
}
