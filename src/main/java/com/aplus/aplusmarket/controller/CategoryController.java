package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.mapper.product.CategoryMapper;
import com.aplus.aplusmarket.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/product/category/all")
    public ResponseEntity selectCategoryAll(){

        ResponseDTO responseDTO =  categoryService.selectAllCategory();
        return ResponseEntity.ok().body(responseDTO);
    }

}
