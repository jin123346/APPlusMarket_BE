package com.aplus.aplusmarket.mapper.product;

import com.aplus.aplusmarket.entity.ProductResponseCard;
import com.aplus.aplusmarket.entity.WishList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.List;
@Mapper
public interface WishListMapper {
    int insertWishList(@Param("productId") Long productId, @Param("userId") Long userId);
    int deleteById(@Param("id") Long id);
    long selectWishList(@Param("productId") Long productId, @Param("userId") Long userId);
    List<ProductResponseCard> productWishList(@Param("userId") long userId);
}
