package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.config.JwtTokenProvider;
import com.aplus.aplusmarket.dto.*;
import com.aplus.aplusmarket.dto.auth.requset.LoginRequest;
import com.aplus.aplusmarket.dto.auth.UserDTO;
import com.aplus.aplusmarket.dto.auth.response.MyInfoUser;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import com.aplus.aplusmarket.service.AuthService;
import com.aplus.aplusmarket.service.UserService;
import com.aplus.aplusmarket.service.WishAndRecentService;
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
        // Refresh Token 삭제를 위한 빈 쿠키 설정

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
        if(refreshToken == null){
            log.info("Refresh Token!! 들어오지 않음 ");
            ResponseDTO responseDTO = ResponseDTO.error(ResponseCode.NO_TOKEN_IN_HERE);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
        log.info("Refresh Token!! 요청 들어옴 "+refreshToken);
        ResponseDTO responseDTO = authService.refreshToken(refreshToken,resp);

        // 실패 응답인 경우


            return ResponseEntity.ok().body(responseDTO);



    }


    //나의 정보 기능
    @GetMapping("/myInfo")
    public ResponseEntity getMyInfo(HttpServletRequest request,@CookieValue(value = "refreshToken",required = false) String refreshToken){
        Long id =(Long)request.getAttribute("id");
        String uid="";
        ResponseDTO responseDTO;
        if(refreshToken == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDTO.error(ResponseCode.LOGIN_REQUIRED));
        }
        log.info("토큰에서 추출된 id : {} ,  쿠키에 저장된 refreshToken : {}",id,refreshToken);
        if(id == null || id == 0){
             uid = authService.getIdWithRefreshToken(refreshToken);
            if(uid==null) {
                //토큰이 제대로 전달되지 않은 경우
                return ResponseEntity.ok().body(ResponseDTO.error(ResponseCode.NO_TOKEN_IN_HERE));
            }
            log.info("토큰에서 추출된 uid : {} ",uid);

            responseDTO = userService.selectUserByUidForMyInfo(uid);

        }else{
            //유저 정보 조회
            responseDTO = userService.selectUserByIdForMyInfo(id);
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
            return ResponseEntity.ok().body( ResponseDTO.error(ResponseCode.LOGIN_REFRESH_TOKEN_NOT_VALIDATED));
        }

        ResponseDTO responseDTO = authService.updateByUserForWithdrawal(id,refreshToken,resp);


        return ResponseEntity.ok().body(responseDTO);
    }


    //하진희 - 2025.03.02 이메일 인증 요청
    @PostMapping("/mail/verification")
    public ResponseEntity emailVerification(@RequestParam String email){
        if(email == null){

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body( ResponseDTO.error(ResponseCode.EMAIL_CODE_NOT_CORRECT));
        }

        return ResponseEntity.ok().body(userService.verificationEmail(email));

    }

    @PostMapping("/mail/resend-verification-code")
    public ResponseEntity resendVerificationCode(@RequestParam String code,@RequestParam String email){
        if(code == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body( ResponseDTO.error(ResponseCode.EMAIL_CODE_NOT_CORRECT));
        }

        return ResponseEntity.ok().body(userService.verifyCode(email,code));

    }





}
