package com.aplus.aplusmarket.repository;

import com.aplus.aplusmarket.document.Products;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.*;


@Repository
public interface ProductsRepository extends MongoRepository<Products,String> {
    List<Products> findAll();
    Optional<Products> findById(String id );
    Optional<Products> findByProductDetailCode(String productDetailCode);
}
