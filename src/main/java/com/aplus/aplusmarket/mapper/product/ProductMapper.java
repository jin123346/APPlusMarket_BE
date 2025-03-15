package com.aplus.aplusmarket.mapper.product;
import com.aplus.aplusmarket.dto.product.requests.ProductModifyRequestDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.entity.ProductResponseCard;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMapper {
    boolean InsertProduct(Product product);
    Product SelectProductById(@Param("id") Long id,@Param("userId") Long userId);
    Optional<Product> SelectProductByIdForModify(Long id);
    List<Product> SelectAllProducts();
    boolean updateProduct(Product product);
    boolean DeleteProduct(Long id);
    List<ProductResponseCard> SelectProductsPage(@Param("pageSize") int pageSize, @Param("lastIndex") int lastIndex,@Param("brand") String brand,@Param("keyword") String keyword);
    Long countProductsForState(@Param("status") String status,@Param("brand") String brand,@Param("keyword") String keyword);
    List<ProductResponseCard> selectProductByIdForStatus(@Param("lastIndex") long lastIndex ,@Param("userId") long userId,@Param("status") String status );
    List<ProductResponseCard> selectProductByIdForCompleted(@Param("lastIndex") long lastIndex ,@Param("userId") long userId,@Param("status") String status );
    int updateReload(Long id);
    int updateStatus(@Param("id") Long id,@Param("status")  String status);
    void updateHit(@Param("id") Long id,@Param("hit")  int hit);
}

