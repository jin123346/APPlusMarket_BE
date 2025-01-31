package com.aplus.aplusmarket.dto.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
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
