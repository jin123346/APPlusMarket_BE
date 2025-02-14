package com.aplus.aplusmarket.dto.product.response;

import com.aplus.aplusmarket.dto.product.Product_ImagesDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseCardDTO { //상품 리스트 화면 출력 화면
    private Long id;
    private String title;
    private String productImage;
    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer price;
    private String status;
    private Boolean isNegotiable;
    private Boolean isPossibleMeetYou;
    private String category;
    private Long sellerId;
    private String registerLocation;
    private String uuidName;
    private String brand;

}
