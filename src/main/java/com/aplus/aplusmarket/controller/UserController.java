package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {



    @PostMapping("/login")
    public String Login(@RequestBody LoginRequest user) {
        log.info("uid : " + user.getUid());
        log.info("password : " + user.getPassword());

        return "Login success";
    }
}
