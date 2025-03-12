package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.CategoryResponseDTO;
import com.aplus.aplusmarket.entity.Category;
import com.aplus.aplusmarket.handler.CustomException;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.mapper.product.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class CategoryService {

   private final CategoryMapper categoryMapper;

   public ResponseDTO selectAllCategory(){
       try{
           List<Category> categoryList=  categoryMapper.SelectParentCategory(0L);

           log.info("결과 : {}",categoryList);
           List<CategoryResponseDTO> categoryResponseDTOS = new ArrayList<>();
           for(Category c : categoryList){
               List<Category> subCategory = categoryMapper.SelectParentCategory(c.getId());
               CategoryResponseDTO categoryResponseDTO = CategoryResponseDTO.builder()
                       .id(c.getId())
                       .categoryName(c.getCategoryName())
                       .subCategoryList(subCategory)
                       .build();

               categoryResponseDTOS.add(categoryResponseDTO);
           }

           return ResponseDTO.success(ResponseCode.CATEGORY_FIND_SUCCESS);

       }catch (Exception e){
           log.error(e);
           throw new CustomException(ResponseCode.CATEGORY_FIND_FAILED);
       }
   }
}
