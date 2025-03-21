package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.RestDocsConfiguration;
import com.aplus.aplusmarket.TestSupport;
import com.aplus.aplusmarket.dto.auth.requset.LoginRequest;
import com.aplus.aplusmarket.service.AuthService;
import com.aplus.aplusmarket.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.class)
class AuthControllerTest extends TestSupport {


    @Test
    @Order(1)
    void login() throws Exception {

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(readJson("/json/user-api/user-login.json")))
                .andExpect(status().isOk());


    }

    @Test
    @Order(2)
    void logout() throws Exception {
        mockMvc.perform(post("/auth/logout?userId=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void deleteUser() throws  Exception{
        mockMvc.perform(delete("/auth/user/delete/test111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void register() throws Exception {


        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-register.json")))
                .andExpect(status().isCreated());


    }

    @Test
    void refresh() {
    }

    @Test
    void getMyInfo() {
    }

    @Test
    void updateUserInfo() {
    }

    @Test
    void updateWithdrawal() {
    }

    @Test
    void emailVerification() {
    }

    @Test
    void resendVerificationCode() {
    }
}