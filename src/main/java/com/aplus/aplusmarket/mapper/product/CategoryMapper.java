package com.aplus.aplusmarket.mapper.product;

import com.aplus.aplusmarket.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface CategoryMapper {

    List<Category> SelectParentCategory(Long id);
    List<Category> selectCategoryById(Long parentId);

}
