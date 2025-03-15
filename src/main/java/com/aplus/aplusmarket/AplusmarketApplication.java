package com.aplus.aplusmarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "com.aplus.aplusmarket.mapper")
public class AplusmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(AplusmarketApplication.class, args);
    }

}
