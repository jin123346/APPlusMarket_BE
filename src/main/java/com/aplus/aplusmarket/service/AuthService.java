package com.aplus.aplusmarket.service;


import com.aplus.aplusmarket.config.JwtTokenProvider;
import com.aplus.aplusmarket.documents.TokenHistory;
import com.aplus.aplusmarket.dto.*;
import com.aplus.aplusmarket.dto.auth.requset.LoginRequest;
import com.aplus.aplusmarket.dto.auth.UserDTO;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import com.aplus.aplusmarket.repository.TokenHistoryRepository;
import com.aplus.aplusmarket.util.TokenEncrpytor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TokenHistoryRepository tokenHistoryRepository;

    private final MongoTemplate mongoTemplate;

    //로그인 서비스
    @Transactional
    public ResponseDTO login(LoginRequest loginRequest, HttpServletResponse resp, HttpServletRequest request) {
            log.info("로그인 시도한 아이디 : {}", loginRequest.getUid());
            Optional<User> opt = userMapper.selectUserByUid(loginRequest.getUid());
             log.info("111111");

        if(opt.isPresent()){
            log.info("22222");
                User user = opt.get();
                //active deactive 확인 여부

            log.info("DB에서 가져온 사용자 정보: {}", user);
                if(!user.getStatus().equals("Active")){
                    log.info("로그인 실패 - not Active, 아이디: {}", loginRequest.getUid());

                    return ResponseDTO.builder()
                            .code(1002)
                            .status("fail")
                            .message("활성화된 계정이 아닙니다.")
                            .build();
                }
            // ✅ 비밀번호 검증 (로그 추가)
                if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    log.info("로그인 실패 - 비밀번호 불일치, 아이디: {}", loginRequest.getUid());
                    return ResponseDTO.builder()
                            .code(1003)
                            .status("fail")
                            .message("계정이 일치하지 않습니다.")
                            .build();
                }
            //  Spring Security 인증 처리

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUid(), loginRequest.getPassword()));
            //  JWT 토큰 생성
            log.info("jwt 토큰생성 성공, 아이디: {}", loginRequest.getUid());

            String accessToken = jwtTokenProvider.createToken(user.getId(),user.getUid(), user.getNickname());
                    String refreshToken = jwtTokenProvider.createRefreshToken(user.getUid(), user.getNickname());
                    UserDTO loginUser = UserDTO.loginUser(user);

            //  응답 헤더 및 쿠키에 토큰 추가

                    resp.setHeader("Authorization", "Bearer " + accessToken);
                    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                            .httpOnly(true)  //  JavaScript에서 접근 불가
                            .secure(false)    // HTTPS에서만 전송
                            .path("/")       // 모든 경로에서 사용 가능
                            .sameSite("Strict") //  CSRF 공격 방지
                            .maxAge(7 * 24 * 60 * 60) // 7일 유지
                            .build();
                            log.info("로그인 성공 아이디 : {}", loginRequest.getUid());
                    resp.addHeader("Set-Cookie", refreshTokenCookie.toString()); // 쿠키를 응답 헤더에 추가

            //  MongoDB에 토큰 저장 (토큰 히스토리)
                    TokenHistory saveTokenToDB = jwtTokenProvider.saveTokenToDB(user.getId(),refreshToken, loginRequest.getDeviceInfo(),request);
                if (saveTokenToDB != null) {
                    log.info("로그인 성공 - 아이디: {}", loginRequest.getUid());
                    return new DataResponseDTO<>(loginUser, 1000, "로그인 성공");
                } else {
                    log.error("MongoDB에 토큰 저장 실패 - 아이디: {}", loginRequest.getUid());
                    return ResponseDTO.builder()
                            .code(1004)
                            .status("fail")
                            .message("로그인 중 오류가 발생했습니다.")
                            .build();
            }

               // return new DataResponseDTO<>(UserDTO.loginUser(user), 1000, "로그인 성공");
//

            }
            //해당하는 user 없음
            return ResponseDTO.builder()
                    .code(1003)
                    .status("fail")
                    .message("계정이 일치하지 않습니다.")
                    .build();


    }

    public ResponseDTO logout(HttpServletResponse response,String refreshToken,long userId){

        try{
            log.info("로그아웃 중 ");
            if(refreshToken != null){
                log.info("로그아웃 중 : 토큰이 null이 아님  ");

                if(userId > 0 ){
                    String hashedUser = TokenEncrpytor.hashUserId(userId);
                    revokeAllTokensByUserId(hashedUser);

                }else{
                    log.info("로그아웃 중 : 토큰히스토리 확인중3  ");

                    String encrypted =  TokenEncrpytor.encrypt(refreshToken);
                    Optional<TokenHistory> opt =  tokenHistoryRepository.findByRefreshToken(encrypted);
                    if(opt.isPresent()){
                        TokenHistory tokenHistory = opt.get();
                        revokeTokensByRefreshToken(tokenHistory);

                    }
                }

            }

            // ✅ Refresh Token 삭제를 위한 빈 쿠키 설정
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(0) // 즉시 삭제
                    .build();

            response.addHeader("Set-Cookie", deleteCookie.toString());



            return ResponseDTO.of("success",1009,"Logged out successfully");
        } catch (Exception e) {
            return ResponseDTO.of("fail",1010,"로그아웃 중 에러 발생 "+e.getMessage());
        }

    }


    public ResponseDTO refreshToken(String refreshToken){

        if (refreshToken == null) {
            log.info("❌ Refresh Token 없음");
            return ErrorResponseDTO.of(1005, "토큰이 존재하지 않습니다.");
        }

        try {
            // ✅ Refresh Token 검증
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                log.info("❌ Refresh Token 검증 실패");
                return ErrorResponseDTO.of(1006, "토큰이 유효하지 않거나 만료되었습니다.");
            }

            // ✅ Refresh Token에서 UID 추출
            String uid = jwtTokenProvider.getUid(refreshToken);
            Optional<User> optionalUser = userMapper.selectUserByUid(uid);

            if (optionalUser.isEmpty()) {
                log.info("❌ User가 존재하지 않음");
                return ErrorResponseDTO.of(1006, "토큰이 유효하지 않거나 만료되었습니다.");
            }

            User user = optionalUser.get();

            // ✅ 새로운 Access Token 발급
            String newAccessToken = jwtTokenProvider.createToken(user.getId(), user.getUid(), user.getNickname());

            // ✅ Refresh Token 기록 업데이트 (MongoDB)
            String encrypted = TokenEncrpytor.encrypt(refreshToken);
            Optional<TokenHistory> opt = tokenHistoryRepository.findByRefreshToken(encrypted);

            if (opt.isPresent()) {
                TokenHistory tokenHistory = opt.get();
                tokenHistory.setRefreshCount(tokenHistory.getRefreshCount() + 1);
                tokenHistoryRepository.save(tokenHistory);

                log.info("✅ Token 업데이트 및 재발급 완료");

                // ✅ 새로운 객체를 생성하여 안전하게 반환
                return DataResponseDTO.of(newAccessToken, 1000, "Access Token이 재발급되었습니다.");
            }

            return ErrorResponseDTO.of(1006, "Refresh Token 기록을 찾을 수 없습니다.");

        } catch (Exception e) {
            log.error("❌ Refresh Token 처리 중 오류 발생: {}", e.getMessage());
            return ErrorResponseDTO.of(1006, "서버 내부 오류가 발생했습니다.");
        }
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

    // ✅ userId를 기준으로 모든 Refresh Token을 revoke = true로 변경
    public void revokeTokensByRefreshToken(TokenHistory tokenHistory) {
            tokenHistory.setRevoked(true);
            tokenHistoryRepository.save(tokenHistory);
//        Query query = new Query();
//        query.addCriteria(Criteria.where("refreshToken").is(refreshToken)); // ✅ 특정 userId 찾기
//        Update update = new Update();
//        update.set("revoked", true); // ✅ revoke = true로 설정
//        mongoTemplate.updateFirst(query, update, TokenHistory.class); // ✅ 하나의 문서만 업데이트
    }


    // ✅ userId를 기준으로 모든 Refresh Token을 revoke = true로 변경
    @Transactional
    public void revokeAllTokensByUserId(String userId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("hashed_userid").is(userId)); // ✅ 특정 userId 찾기
        Update update = new Update();
        update.set("revoked", true); // ✅ revoke = true로 설정

        try{
            mongoTemplate.updateMulti(query, update, TokenHistory.class); // ✅ 여러 개 업데이트

        }catch (Exception e){
            log.error("Refresh Token 무효화 중 오류 발생 - userId: {}", userId, e);
            throw new RuntimeException("Refresh Token 무효화 중 오류 발생");
        }
    }

    public String getIdWithRefreshToken(String refresh){
        if(refresh == null){
            return null;
        }

        log.info("uid 확인 ! {}",jwtTokenProvider.getUid(refresh));
        return jwtTokenProvider.getUid(refresh);
    }
}
