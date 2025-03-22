package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.TestSupport;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.response.AddressBookResponseDTO;
import com.aplus.aplusmarket.dto.auth.response.MyInfoUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest extends TestSupport {

    static long addressId;


    @Test
    @Order(1)
    void findUid() throws Exception {
        mockMvc.perform(post("/find/uid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-find-uid.json")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void findPass() throws Exception {
        mockMvc.perform(post("/find/pass")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-find-password.json")))
                .andExpect(status().isOk());
    }


    @Test
    @Order(3)
    void changePass() throws Exception {
        mockMvc.perform(put("/find/pass/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-change-password.json")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void insertAddress() throws Exception {
        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");


        MvcResult result = (MvcResult) mockMvc.perform(post("/my/address/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokens.get("Authorization"))
                        .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken")))
                        .content(readJson("/json/user-api/user-address-register.json")))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseDTO responseDTO = objectMapper.readValue(responseBody, ResponseDTO.class);

        // data 안에 addressId가 있다고 가정
        Integer data = (Integer) responseDTO.getData();
        addressId = data.longValue();
    }





    @Test
    @Order(5)
    void getAddressList() throws Exception {
        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");


        mockMvc.perform(get("/my/address/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokens.get("Authorization"))
                        .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken"))))
                .andExpect(status().isOk());
    }


    @Test
    @Order(6)
    void modifyAddress() throws Exception {
        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");

        String json = readJson("/json/user-api/user-address-register.json");

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        AddressBookResponseDTO addressBookResponseDTO = objectMapper.readValue(json, AddressBookResponseDTO.class);
        addressBookResponseDTO.setId(addressId);
        addressBookResponseDTO.setPostcode("789456");
        System.out.println(addressBookResponseDTO);


        mockMvc.perform(put("/my/address/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokens.get("Authorization"))
                        .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken")))
                        .content(objectMapper.writeValueAsString(addressBookResponseDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void deleteAddress() throws Exception {
        Map<String, Object> tokens = performLoginAndGetTokens("/json/user-api/user-login.json");



        mockMvc.perform(delete("/my/address/10/deleted/"+addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokens.get("Authorization"))
                        .cookie(new Cookie("refreshToken", (String) tokens.get("refreshToken"))))
                .andExpect(status().isOk());
    }
}