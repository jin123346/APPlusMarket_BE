package com.aplus.aplusmarket.service;


import com.aplus.aplusmarket.config.JwtTokenProvider;
import com.aplus.aplusmarket.dto.*;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/*
    2025.1.27 하진희 로그인 및 회원가입 서비스 기능 구현
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    //로그인 서비스
    @Transactional
    public ResponseDTO login(LoginRequest loginRequest, HttpServletResponse resp) {
            log.info("로그인 시도한 아이디 : {}", loginRequest.getUid());
            Optional<User> opt = userMapper.selectUserByUid(loginRequest.getUid());
             log.info("111111");

        if(opt.isPresent()){
            log.info("22222");
                User user = opt.get();
                //active deactive 확인 여부
                log.info("status :{}",user.getStatus());
                if(!user.getStatus().equals("Active")){
                    return ResponseDTO.builder()
                            .code(1002)
                            .status("fail")
                            .message("활성화된 계정이 아닙니다.")
                            .build();
                }
                //비밀번호 일치 여부
                if(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
                    String accessToken = jwtTokenProvider.createToken(user.getId(),user.getUid(), user.getNickname());
                    String refreshToken = jwtTokenProvider.createRefreshToken(user.getUid(), user.getNickname());


                    Map<String, Object> tokens = new HashMap<>();
                    tokens.put("token", accessToken);
                    tokens.put("refresh_token", refreshToken);


                    Cookie cookie = new Cookie("refresh_token",refreshToken);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(60*60*24*7);
                    resp.addCookie(cookie);
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUid(), loginRequest.getPassword()));
                    log.info("로그인 성공 아이디 : {}", loginRequest.getUid());
                    return new DataResponseDTO<>(tokens,1000,"로그인 성공");
                }



            }
            //해당하는 user 없음
            return ResponseDTO.builder()
                    .code(1003)
                    .status("fail")
                    .message("계정이 일치하지 않습니다.")
                    .build();


    }


    //회원가입
    public ResponseDTO insertUser(UserDTO userDTO){
        try {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encodedPassword);
            User savedUser = userDTO.register();
            userMapper.insertUser(savedUser);
            long id = savedUser.getId();

           return ResponseDTO.of("succeess",1100,"회원가입 성공 : "+id);
        }catch (Exception e){
            log.error(e);
           return ErrorResponseDTO.of(1103, "회원가입 실패 :"+e.getMessage());
        }

    }


    //회원가입 인증절차
    public boolean registerValidation( String type,String value){
        Optional<User> opt = Optional.empty();
        switch (type){
            case "email":
                opt = userMapper.selectUserByEmail(value);
                break;
            case "uid":
                opt = userMapper.selectUserByUid(value);
                break;
            case "hp":
                opt = userMapper.selectUserByHp(value);
                break;
            default:
                log.info("유효하지 않은 타입 : {}",type);
                break;
        }

        if(opt.isPresent()){
            //사용할 수 없는 값
            log.info("이미 존재하는 데이터입니다.");
            return false;
        }else{
            log.info("사용가능한 데이터입니다.");
            return true;
        }

    }
}
