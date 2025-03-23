package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.TestSupport;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest extends TestSupport {

    @Test
    @Order(1)
    void getProducts() throws Exception{
        mockMvc.perform(get("/product/listpage"))
                .andExpect(status().isOk());

    }





    @Test
    void insertProduct() throws Exception {
    }


    // 상품 상세보기
    @Test
    void selectProductById() throws Exception {
    }


    //로그인 필요
    @Test
    void selectProductForSelling() throws Exception {
    }

    @Test
    void selectProductForCompleted() throws Exception{
    }


    @Test
    void reloadProduct() throws Exception{
    }

    @Test
    void testReloadProduct() throws Exception{
    }

    @Test
    void modifyProduct() throws Exception{
    }

    @Test
    void updateProductForModify() throws Exception{
    }

    @Test
    void updateWish() throws Exception{
    }

    @Test
    void getWishList() throws Exception{
    }
}