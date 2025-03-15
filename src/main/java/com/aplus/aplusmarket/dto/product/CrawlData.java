package com.aplus.aplusmarket.dto.product;

import com.aplus.aplusmarket.entity.Brand;
import com.aplus.aplusmarket.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrawlData {

    private String name; // 제품명
    private String modelCode ; // 제품코드(색상포함 코드인듯)
    private String mdlNm ; // 제품코드
    private  String priceStr ;
    private String goodsId ;
    private String categoryName ;
    private String brandName ;
    private String[] priceDat ;
    private int size;
    private String originalPrice ;
    private  String salePrice;
    private String productUrl ; // 상세 페이지 URL

}
