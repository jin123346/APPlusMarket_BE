package com.aplus.aplusmarket.dto.product;

import com.aplus.aplusmarket.entity.Product_Images;
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

    public static Product_ImagesDTO toDTO(Product_Images image){

        return Product_ImagesDTO.builder()
                .id(image.getId())
                .productId(image.getProductId())
                .originalName(image.getOriginalName())
                .uuidName(image.getUuidName())
                .sequence(image.getSequence())
                .build();
    }

}
