package com.aplus.aplusmarket.mapper.product;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.entity.ProductResponseCard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    boolean InsertProduct(Product product);
    Product SelectProductById(Long id);
    List<Product> SelectAllProducts();
    boolean UpdateProduct(Product product);
    boolean DeleteProduct(Long id);
    List<ProductResponseCard> SelectProductsPage(@Param("pageSize") int pageSize, @Param("offset") int offset);
}

