package com.aplus.aplusmarket.mapper.product;

import com.aplus.aplusmarket.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface CategoryMapper {

    List<Category> SelectParentCategory(Long id);
    List<Category> selectCategoryById(Long parentId);
    Optional<Category> selectCategoryByName(@Param(value = "name") String name);
}
