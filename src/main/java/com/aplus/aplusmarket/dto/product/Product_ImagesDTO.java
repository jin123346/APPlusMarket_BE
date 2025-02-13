package com.aplus.aplusmarket.dto.product;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product_ImagesDTO {
    private Long id;            // PK (이미지 고유 번호)
    private Long productId;     // 상품 ID (FK)
    private String originalName; // 업로드된 실제 파일명
    private String uuidName;     // 서버에서 저장할 고유 파일명
    private Integer sequence; //이미지 순서
}
