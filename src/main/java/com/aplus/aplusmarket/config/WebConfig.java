package com.aplus.aplusmarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    2025.1.25 하진희 cors 설정
    2025.1.29 하진희 cors 일시적 전체 허용 ( chrome 사용을 위해서 허용함)
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOriginPatterns("http://192.168.*.*:8080", "http://10.0.2.2:8080", "http://localhost:8080", "http://127.0.0.1:8080", "http://localhost:3000")
               .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
               // .allowCredentials(true);
                .allowCredentials(false);
    }
}
