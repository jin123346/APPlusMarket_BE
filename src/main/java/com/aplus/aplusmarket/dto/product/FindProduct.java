package com.aplus.aplusmarket.dto.product;

import com.aplus.aplusmarket.document.Products;
import com.aplus.aplusmarket.entity.Brand;
import com.aplus.aplusmarket.entity.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindProduct {
    @Id
    private String id;
    private Brand brand;
    private Category category;
    private String name;
    private String productCode;
    private String productDetailCode;
    private double originalPrice;
    private double finalPrice;
    private String productUrl;
    private String brandName;
    private String categoryName;

    public static FindProduct toDTO(Products products){
        return FindProduct.builder()
                .id(products.getId())
                .brandName(products.getBrand() == null  ? null : products.getBrand().getName())
                .categoryName(products.getCategory() == null ? null : products.getCategory().getCategoryName())
                .finalPrice(products.getFinalPrice())
                .originalPrice(products.getOriginalPrice())
                .productUrl(products.getProductUrl())
                .name(products.getName())
                .productCode(products.getProductCode())
                .productDetailCode(products.getProductDetailCode())
                .build();
    }
}
