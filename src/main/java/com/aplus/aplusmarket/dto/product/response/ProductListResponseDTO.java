package com.aplus.aplusmarket.dto.product.response;

import com.aplus.aplusmarket.entity.ProductResponseCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponseDTO {
    private Boolean isFirst;
    private Boolean isLast;
    private long lastIndex;
    private int size;
    private int totalPage;
    private int page;
    private List<ProductResponseCardDTO> products;



}
