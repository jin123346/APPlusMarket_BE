package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.config.JwtTokenProvider;
import com.aplus.aplusmarket.dto.*;
import com.aplus.aplusmarket.dto.auth.requset.LoginRequest;
import com.aplus.aplusmarket.dto.auth.UserDTO;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import com.aplus.aplusmarket.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
    20024.01.27 하진희 user login/ register controller
 */

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse resp, HttpServletRequest request) {
        log.info("Login request: " + loginRequest);
        ResponseDTO response = authService.login(loginRequest,resp,request);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                    @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                    HttpServletResponse response) {
        // ✅ Refresh Token 삭제를 위한 빈 쿠키 설정

        ResponseDTO responseDTO = authService.logout(response,refreshToken,userId);
        return ResponseEntity.ok().body(responseDTO);
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO userInfo){
        log.info("회원등록 시작");
        ResponseDTO responseDTO = authService.insertUser(userInfo);

        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("/refresh")
    public ResponseEntity refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken){
        if(refreshToken == null){
            ResponseDTO responseDTO = ErrorResponseDTO.of(1005,"토큰이 존재하지않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
        ResponseDTO responseDTO = authService.refreshToken(refreshToken);

        // ✅ 실패 응답인 경우
        if (responseDTO instanceof ErrorResponseDTO) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }

        // ✅ 성공 응답인 경우 (새 Access Token 포함)
        if (responseDTO instanceof DataResponseDTO) {
            DataResponseDTO<String> dataResponseDTO = (DataResponseDTO<String>) responseDTO;
            String newAccessToken = dataResponseDTO.getData();

            // ✅ 클라이언트가 JSON 응답에서도 새 Access Token을 받을 수 있도록 추가
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + newAccessToken)
                    .body(ResponseDTO.of("success", 1000, "Access Token이 재발급되었습니다."));
        }

        // ✅ 예외 상황 (이론적으로 발생하지 않지만, 예외 처리를 위해 추가)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.of(1007, "알 수 없는 오류가 발생했습니다."));

    }


}
