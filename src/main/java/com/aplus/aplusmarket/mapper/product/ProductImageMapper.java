package com.aplus.aplusmarket.mapper.product;

import com.aplus.aplusmarket.entity.Product_Images;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductImageMapper {
    boolean InsertProductImage(Product_Images productImages);
    List<Product_Images> SelectProductImageByProductId(Long productId);
    Product_Images SelectProductImageById(Long id);
    boolean DeleteProductImageByProductId(Long productId);
    int deleteById(Long id);
    int updateSequence(@Param("itemId") Long itemId, @Param("sequence") int sequence);
    int deleteByProductId(@Param("productId") Long productId);

}
