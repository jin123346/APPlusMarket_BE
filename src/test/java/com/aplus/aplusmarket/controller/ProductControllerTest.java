package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.TestSupport;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;


import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest extends TestSupport {

    static long productId;


    @Test
    @Order(1)
    void getProducts() throws Exception{
        mockMvc.perform(get("/product/listpage")
                        .content(readJson("/json/product-api/product-list-page.json")))
                .andExpect(status().isOk());

    }


    @Test
    @Order(2)
    void insertProduct() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "images", // 컨트롤러의 MultipartFile 파라미터 이름과 같아야 함
                "test.png", // 파일 이름
                "image/png", // Content-Type
                "테스트이미지".getBytes() // 바이트 배열 또는 실제 파일 내용을 넣어도 됨
        );


        MvcResult result = mockMvc.perform(multipart("/product/insert") // <-- @PostMapping("/insert")와 맞춰야 함
                        .file(file)
                        .param("title", "글제목")
                        .param("productName", "상품이름")
                        .param("content", "글 내용")
                        .param("price", "15000000")
                        .param("sellerId", "10")
                        .param("isNegotiable", "true")
                        .param("isPossibleMeetYou", "true")
                        .param("category", "노트북")
                        .param("brand", "삼성")
                        .param("findProductId", "")
                        .param("registerLocation", "부산 진구"))
                .andExpect(status().isOk()).andReturn();



        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDTO responseDTO = objectMapper.readValue(responseBody, ResponseDTO.class);

        // data 안에 addressId가 있다고 가정
        Integer data = (Integer) responseDTO.getData();

        productId = data.longValue();
    }

    @Order(3)
    @Test
    void getProductForModify() throws Exception{
        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");

        mockMvc.perform(get("/product/modify/{productId}/{userId}",productId,10L)
                .header("Authorization", tokens.get("Authorization"))
                .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken"))))
                .andExpect(status().isOk());

    }

    @Test
    @Order(4)
    void modifyProduct() throws Exception{
        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");

        String json = readJson("/json/product-api/product-modify.json");

        // JSON을 "data" 라는 이름의 RequestPart로 감싸기
        MockMultipartFile jsonPart = new MockMultipartFile(
                "data",              // 컨트롤러에서 @RequestPart("data")로 받음
                "data",// 파일 이름 (의미 없음)
                "application/json",  // MIME 타입
                json.getBytes(StandardCharsets.UTF_8)      // 실제 JSON 내용
        );


        mockMvc.perform(multipart("/product/modify/{productId}/{userId}",productId,10L)
                        .file(jsonPart)
                        .header("Authorization", tokens.get("Authorization"))
                        .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken"))))
                .andExpect(status().isOk());

    }

    //선택한 상품 정보 가지고 오기
    @Test
    @Order(5)
    void selectProductById() throws Exception {

        mockMvc.perform(get("/product/{id}?userId=10", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @Order(8)//판매중 상품
    void selectProductForSelling() throws Exception{
        mockMvc.perform(get("/product/on-sale", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/product-api/product-onsale.json")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(9) // 관심상품 등록 및 해제
    void updateWish() throws Exception{

        mockMvc.perform(put("/product/wish/{productId}/{userId}",productId, 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)// 관심상품 목록 가져오기
    void getWishList() throws Exception{

        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");

        mockMvc.perform(get("/product/wish/list",productId, 10)
                           .contentType(MediaType.APPLICATION_JSON)
                           .header("Authorization", tokens.get("Authorization"))
                           .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken"))))
                .andExpect(status().isOk());


    }

    @Test
    @Order(11) //끌어올리기
    void reload() throws Exception{
        mockMvc.perform(put("/product/reload/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    //    Active, Reserved, Sold, Hidden, Deleted


    @Test
    @Order(15)
    void updateDeActiveForProduct() throws Exception {
        mockMvc.perform(put("/product/{productId}/{status}", 53,"Deleted")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(13)
    void updateDeSoldForProduct() throws Exception {
        mockMvc.perform(put("/product/{productId}/{status}", productId,"Sold")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    void updateHiddenForProduct() throws Exception {
        mockMvc.perform(put("/product/{productId}/{status}", productId,"Hidden")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    void deletedProduct() throws Exception {
        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");

        mockMvc.perform(delete("/product/delete/{productId}/{userId}", 50,10)
                        .header("Authorization", tokens.get("Authorization"))
                        .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }








}