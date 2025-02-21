package com.aplus.aplusmarket.dto.chat;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCardDTO {

    private int productId;
    private String productName;
    private int price;
    private String thumbnailImage;
    private Boolean isNegotiable;
}
