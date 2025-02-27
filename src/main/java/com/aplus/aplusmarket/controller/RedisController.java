package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.ProductCardDTO;
import com.aplus.aplusmarket.dto.product.request;
import com.aplus.aplusmarket.service.WishAndRecentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class RedisController {
    private final WishAndRecentService wishAndRecentService;

    @PostMapping("/recent/product")
    public void addRecentProduct(@RequestBody ProductCardDTO dto, HttpServletRequest request){

        wishAndRecentService.addRecentList(dto);


    }

    @GetMapping("/recent/list")
    public ResponseEntity getRecentList( HttpServletRequest request){

        Long user = (Long) request.getAttribute("id");
        log.info("요청한 유저 : {}",user);
        DataResponseDTO responseDTO = wishAndRecentService.getRecentProducts(user);

        return ResponseEntity.ok(responseDTO);
    }

}
