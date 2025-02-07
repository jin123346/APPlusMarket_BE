package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.config.JwtTokenProvider;
import com.aplus.aplusmarket.dto.*;
import com.aplus.aplusmarket.dto.auth.requset.LoginRequest;
import com.aplus.aplusmarket.dto.auth.UserDTO;
import com.aplus.aplusmarket.dto.auth.response.MyInfoUser;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import com.aplus.aplusmarket.service.AuthService;
import com.aplus.aplusmarket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.OutputKeys;
import java.util.Optional;

/*
    20024.01.27 하진희 user login/ register controller
    20024.02.04 하진희 myinfo 부분 추가
 */

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final UserService userService;


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

        log.info("logout Token!!"+refreshToken);

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
    public ResponseEntity refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken,HttpServletResponse resp){
        log.info("Refresh Token!! 요청 들어옴 "+refreshToken);
        if(refreshToken == null){
            ResponseDTO responseDTO = ErrorResponseDTO.of(1005,"토큰이 존재하지않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
        ResponseDTO responseDTO = authService.refreshToken(refreshToken,resp);

        // ✅ 실패 응답인 경우
        if (responseDTO instanceof ErrorResponseDTO) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }

        // ✅ 성공 응답인 경우 (새 Access Token 포함)
        if (responseDTO instanceof DataResponseDTO) {
            DataResponseDTO dataResponseDTO = (DataResponseDTO) responseDTO;

            // ✅ 클라이언트가 JSON 응답에서도 새 Access Token을 받을 수 있도록 추가
            return ResponseEntity.ok()
                    .body(dataResponseDTO);
        }

        // ✅ 예외 상황 (이론적으로 발생하지 않지만, 예외 처리를 위해 추가)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.of(1007, "알 수 없는 오류가 발생했습니다."));

    }


    //나의 정보 기능
    @GetMapping("/myInfo")
    public ResponseEntity getMyInfo(HttpServletRequest request,@CookieValue(value = "refreshToken") String refreshToken){
        Long id =(Long)request.getAttribute("id");
        log.info("토큰에서 추출된 id : {} ,  쿠키에 저장된 refreshToken : {}",id,refreshToken);
        String uid="";
        ResponseDTO responseDTO;
        if(id == null || id == 0){
             uid = authService.getIdWithRefreshToken(refreshToken);
            if(uid==null) {
                //토큰이 제대로 전달되지 않은 경우
                return ResponseEntity.ok().body(ErrorResponseDTO.of(1202, "토큰이 사라짐"));
            }
            responseDTO = userService.selectUserByUidForMyInfo(uid);

        }else{
            //유저 정보 조회
            responseDTO = userService.selectUserByIdForMyInfo(id);
        }


        // 조회시 에러 상황
        if(responseDTO instanceof  ErrorResponseDTO){
            return ResponseEntity.ok().body((ErrorResponseDTO)responseDTO);
        }

        return ResponseEntity.ok().body(responseDTO);

    }

    // 나의 정보 수정 요청
    @PostMapping("/myInfo")
    public ResponseEntity updateUserInfo(@RequestBody MyInfoUser myInfoUser){

       ResponseDTO responseDTO =  userService.updateUserByIdForMyInfo(myInfoUser);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/withdrawal/{id}")
    public ResponseEntity updateWithdrawal(@PathVariable("id") long id,HttpServletRequest request,@CookieValue(value = "refreshToken", required = false) String refreshToken,HttpServletResponse resp){
       // ResponseDTO responseDTO = userService;
        long token_id = (long) request.getAttribute("id");
        if(id != token_id){
            return ResponseEntity.ok().body( ErrorResponseDTO.of(1125 ,"토큰정보와 일치하지 않습니다."));
        }

        ResponseDTO responseDTO = authService.updateByUserForWithdrawal(id,refreshToken,resp);


        return ResponseEntity.ok().body(responseDTO);
    }





}
