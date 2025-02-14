package com.aplus.aplusmarket.dto.product.requests;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductListRequestDTO {
    private Long userId;
    private long lastIndex;
    private long pageSize;
}
