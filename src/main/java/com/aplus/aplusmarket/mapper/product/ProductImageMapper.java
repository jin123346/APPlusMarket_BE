package com.aplus.aplusmarket.mapper.product;

import com.aplus.aplusmarket.entity.Product_Images;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductImageMapper {
    boolean InsertProductImage(Product_Images productImages);
    List<Product_Images> SelectProductImageByProductId(Long productId);
    boolean DeleteProductImageByProductId(Long productId);
}
