package com.aplus.aplusmarket.dto.product.requests;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO { // 상품 상세 정보 등록 데이터
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
    private Boolean isNegotiable;
    private Boolean isPossibleMeetYou;
    private String category;
}
