package com.aplus.aplusmarket;


import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfiguration.class)
public class TestSupport {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected RestDocumentationResultHandler write;
    @Autowired
    @Qualifier("webApplicationContext")
    protected WebApplicationContext  resourceLoader;


//    @Sql(scripts = "/sql/cleanup-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @BeforeEach
    void setup(final WebApplicationContext context,
               final RestDocumentationContextProvider provider) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(write)
                .build();

    }

    protected String readJson(final String path) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:"+ path);

        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    protected Map<String, Object> performLoginAndGetTokens(String loginJsonPath) throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readJson(loginJsonPath)))
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

        Map<String, Object> result = new HashMap<>();
        result.put("Authorization", authorizationHeader);
        result.put("refreshToken", refreshToken);
        return result;
    }
}

