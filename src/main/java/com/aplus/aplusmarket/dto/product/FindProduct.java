package com.aplus.aplusmarket.dto.product;

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
}
