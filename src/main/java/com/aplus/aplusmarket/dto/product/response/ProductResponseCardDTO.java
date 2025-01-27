package com.aplus.aplusmarket.dto.product.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseCardDTO {
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
}
