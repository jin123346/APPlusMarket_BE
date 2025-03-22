package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.RestDocsConfiguration;
import com.aplus.aplusmarket.TestSupport;
import com.aplus.aplusmarket.dto.auth.requset.LoginRequest;
import com.aplus.aplusmarket.dto.auth.response.MyInfoUser;
import com.aplus.aplusmarket.service.AuthService;
import com.aplus.aplusmarket.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
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
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    void refresh() throws Exception {

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-login.json")))
                .andExpect(status().isOk())
                .andReturn();

        // 2. 응답에서 Set-Cookie 헤더에서 refreshToken 추출
        String setCookieHeader = loginResult.getResponse().getHeader("Set-Cookie");
        assertNotNull(setCookieHeader); // 쿠키가 있어야 함

        // refreshToken=value; Path=/; ...
        String refreshToken = Arrays.stream(setCookieHeader.split(";"))
                .filter(token -> token.trim().startsWith("refreshToken="))
                .findFirst()
                .map(token -> token.replace("refreshToken=", ""))
                .orElse(null);

        // 3. refreshToken 쿠키를 가지고 요청
        mockMvc.perform(get("/auth/refresh")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void logout() throws Exception {
        mockMvc.perform(post("/auth/logout?userId=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



    @Test
    @Order(4)
    void deleteUser() throws  Exception{
        mockMvc.perform(delete("/auth/user/delete/test111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void register() throws Exception {


        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-register.json")))
                .andExpect(status().isCreated());


    }



    @Test
    @Order(6)
    void getMyInfo() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-login.json")))
                .andExpect(status().isOk())
                .andReturn();

        // 2. 응답에서 Set-Cookie 헤더에서 refreshToken 추출
        String setCookieHeader = loginResult.getResponse().getHeader("Set-Cookie");
        assertNotNull(setCookieHeader); // 쿠키가 있어야 함

        // refreshToken=value; Path=/; ...
        String refreshToken = Arrays.stream(setCookieHeader.split(";"))
                .filter(token -> token.trim().startsWith("refreshToken="))
                .findFirst()
                .map(token -> token.replace("refreshToken=", ""))
                .orElse(null);

        // 3. refreshToken 쿠키를 가지고 요청
        mockMvc.perform(get("/auth/myInfo?userId=10")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk());



    }

    @Test
    @Order(7)
    void updateUserInfo() throws Exception {
        String json = readJson("/json/user-api/user-update-my-info.json");

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MyInfoUser user = objectMapper.readValue(json, MyInfoUser.class);

        String newNick = "test_"+LocalDateTime.now();
        user.setNickName(newNick);

        mockMvc.perform(post("/auth/myInfo")
                       .contentType(MediaType.APPLICATION_JSON)  // 중요
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void updateWithdrawal() throws Exception {

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-login.json")))
                .andExpect(status().isOk())
                .andReturn();

        String setCookieHeader = loginResult.getResponse().getHeader("Set-Cookie");
        String authorizationHeader = loginResult.getResponse().getHeader("Authorization");

        assertNotNull(setCookieHeader);
        assertNotNull(authorizationHeader);

        String refreshToken = Arrays.stream(setCookieHeader.split(";"))
                .filter(token -> token.trim().startsWith("refreshToken="))
                .findFirst()
                .map(token -> token.replace("refreshToken=", ""))
                .orElse(null);

        mockMvc.perform(get("/auth/withdrawal/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk());
    }


    @Test
    @Order(9)
    void revokeUser() throws Exception {
        mockMvc.perform(post("/auth/revoke")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson("/json/user-api/user-revoke.json")))
                .andExpect(status().isOk());

    }

    @Test
    void emailVerification() throws Exception {
    }

    @Test
    void resendVerificationCode() throws Exception {
    }


}