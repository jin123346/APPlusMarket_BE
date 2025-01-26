package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.LoginRequest;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.UserDTO;
import com.aplus.aplusmarket.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    20024.01.27 하진희 user login/ register controller
 */

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse resp) {
        log.info("Login request: " + loginRequest);
        ResponseDTO response = authService.login(loginRequest,resp);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO userInfo){

        ResponseDTO responseDTO = authService.insertUser(userInfo);

        return ResponseEntity.ok().body(responseDTO);
    }


}
