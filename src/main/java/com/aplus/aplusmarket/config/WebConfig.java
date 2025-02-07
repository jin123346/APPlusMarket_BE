package com.aplus.aplusmarket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    2025.1.25 하진희 cors 설정
    2025.1.29 하진희 cors 일시적 전체 허용 ( chrome 사용을 위해서 허용함)
    2025.02.04 이도영 파일 경로 추가
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload.path}") // application.yml에서 설정된 상대 경로 가져오기
    private String uploadPath;
    private final String USER_DIR = System.getProperty("user.dir");
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 상대 경로를 사용하여 정적 리소스로 접근 가능하도록 설정
        registry.addResourceHandler("uploads/**")
                .addResourceLocations("file:" + USER_DIR + "/" + uploadPath + "/");
    }

}
