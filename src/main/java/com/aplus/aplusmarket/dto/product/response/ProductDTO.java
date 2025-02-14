package com.aplus.aplusmarket.dto.product.response;

import com.aplus.aplusmarket.dto.product.FindProduct;
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
public class ProductDTO { // 상품 상세 정보 출력 데이터
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
    private String brand;
    private List<Product_ImagesDTO> images;
    private FindProduct findProduct;
    private String location;
}
