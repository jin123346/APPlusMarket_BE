package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.chat.ProductCardDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RedisController {

    @PostMapping("/recent/product")
    public void addRecentProduct(@RequestBody ProductCardDTO dto){



    }

}
