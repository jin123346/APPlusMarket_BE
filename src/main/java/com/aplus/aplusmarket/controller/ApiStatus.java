package com.aplus.aplusmarket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiStatus {
    private final DataSource dataSource;
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/status")
    public ResponseEntity checkStatus(){

        Map<String,Object> status = new HashMap<>();
        status.put("status","UP");
        try(Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT 1");
            ResultSet rs = stmt.executeQuery()){
            if(rs.next()){
                status.put("database","UP");

            }else{
                status.put("database","Down");

            }

        }catch (Exception e){
            status.put("database","Down");
        }

        try{
            redisTemplate.opsForValue().set("statusCheck","OK");
            String redisResponse = (String) redisTemplate.opsForValue().get("statusCheck");
            status.put("redis",redisResponse != null ? "UP" : "DOWN");
        }catch (Exception e){
            status.put("redis","DOWN");
        }

        Runtime runtime  = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory())/ (1024 * 1024);
        status.put("memoryUsegeMB",usedMemory);

        boolean allUp = status.values().stream().allMatch(o -> o.equals("UP")||o.equals(usedMemory));
        HttpStatus httpStatus= allUp? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return new ResponseEntity<>(status,httpStatus);

    }


}
