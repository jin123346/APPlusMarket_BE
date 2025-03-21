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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


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
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(write)
                .build();

    }

    protected String readJson(final String path) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:"+ path);

        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}

