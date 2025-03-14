package com.aplus.aplusmarket.service;


import com.aplus.aplusmarket.config.JwtTokenProvider;
import com.aplus.aplusmarket.documents.TokenHistory;
import com.aplus.aplusmarket.dto.*;
import com.aplus.aplusmarket.dto.auth.requset.LoginRequest;
import com.aplus.aplusmarket.dto.auth.UserDTO;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.handler.CustomException;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import com.aplus.aplusmarket.repository.TokenHistoryRepository;
import com.aplus.aplusmarket.util.TokenEncrpytor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
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

import java.time.LocalDateTime;
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
    private final WishAndRecentService wishAndRecentService;

    //로그인 서비스

    /**
     * @param loginRequest
     * @param resp
     * @param request
     * @return
     */
    @Transactional
    public ResponseDTO login(LoginRequest loginRequest, HttpServletResponse resp, HttpServletRequest request) {


        try{
            log.info("로그인 시도한 아이디 : {}", loginRequest.getUid());
            Optional<User> opt = userMapper.selectUserByUid(loginRequest.getUid());
            log.info("111111");

            if (opt.isPresent()) {
                log.info("22222");
                User user = opt.get();
                //active deactive 확인 여부

                log.info("DB에서 가져온 사용자 정보: {}", user);
                if (!user.getStatus().equals("Active")) {
                    log.info("로그인 실패 - not Active, 아이디: {}", loginRequest.getUid());

                    return ResponseDTO.error(ResponseCode.LOGIN_ACCOUNT_LOCKED);

                }
                // 비밀번호 검증 (로그 추가)
                if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    log.info("로그인 실패 - 비밀번호 불일치, 아이디: {}", loginRequest.getUid());
                    return ResponseDTO.error(ResponseCode.LOGIN_PASSWORD_ERROR);


                }
                //  Spring Security 인증 처리

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUid(), loginRequest.getPassword()));
                //  JWT 토큰 생성
                log.info("jwt 토큰생성 성공, 아이디: {}", loginRequest.getUid());

                String accessToken = jwtTokenProvider.createToken(user.getId(), user.getUid(), user.getNickname(),user.getProfileImg());
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
                TokenHistory saveTokenToDB = jwtTokenProvider.saveTokenToDB(user.getId(), refreshToken, loginRequest.getDeviceInfo(), request);
                if (saveTokenToDB != null) {

                    wishAndRecentService.mergeGuestDataToUser(loginRequest.getTempUserId(),loginUser.getId());

                    log.info("로그인 성공 - 아이디: {}", loginRequest.getUid());
                    return ResponseDTO.success(ResponseCode.LOGIN_SUCCESS,loginUser);
                }

            }
            return ResponseDTO.error(ResponseCode.LOGIN_ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.LOGIN_INTERNAL_SERVER_ERROR);
        }



    }

    /**
     * @param response
     * @param refreshToken
     * @param userId
     * @return
     */
    public ResponseDTO logout(HttpServletResponse response, String refreshToken, long userId) {

        try {
            log.info("로그아웃 중 ");
            if (refreshToken != null) {
                log.info("로그아웃 중 : 토큰이 null이 아님  ");

                if (userId > 0) {
                    String hashedUser = TokenEncrpytor.hashUserId(userId);
                    revokeAllTokensByUserId(hashedUser);

                } else {
                    log.info("로그아웃 중 : 토큰히스토리 확인중3  ");

                    String encrypted = TokenEncrpytor.encrypt(refreshToken);
                    Optional<TokenHistory> opt = tokenHistoryRepository.findByRefreshToken(encrypted);
                    if (opt.isPresent()) {
                        TokenHistory tokenHistory = opt.get();
                        revokeTokensByRefreshToken(tokenHistory);

                    }
                }

            }

            // Refresh Token 삭제를 위한 빈 쿠키 설정
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(0) // 즉시 삭제
                    .build();

            response.addHeader("Set-Cookie", deleteCookie.toString());


            return ResponseDTO.success(ResponseCode.LOGOUT_SUCCESS);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.LOGIN_INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * @param refreshToken
     * @return
     */
    public ResponseDTO refreshToken(String refreshToken,HttpServletResponse resp) {

        if (refreshToken == null) {
            log.info("❌ Refresh Token 없음");
            return ResponseDTO.error(ResponseCode.LOGIN_REFRESH_TOKEN_NOT_FOUND);

        }

        try {
            // Refresh Token 검증
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                log.info("❌ Refresh Token 검증 실패");
                return ResponseDTO.error(ResponseCode.LOGIN_REFRESH_TOKEN_NOT_FOUND);
            }

            // Refresh Token에서 UID 추출
            String uid = jwtTokenProvider.getUid(refreshToken);
            Optional<User> optionalUser = userMapper.selectUserByUid(uid);

            if (optionalUser.isEmpty()) {
                log.info("❌ User가 존재하지 않음");
                return ResponseDTO.error(ResponseCode.LOGIN_REFRESH_TOKEN_NOT_VALIDATED);
            }

            User user = optionalUser.get();


            // 새로운 Access Token 발급
            String newAccessToken = jwtTokenProvider.createToken(user.getId(), user.getUid(), user.getNickname(),user.getProfileImg());

            log.info("새로은 AccessToken 생성");

            // Refresh Token 기록 업데이트 (MongoDB)
            String encrypted = TokenEncrpytor.encrypt(refreshToken);
            Optional<TokenHistory> opt = tokenHistoryRepository.findByRefreshToken(encrypted);

            if (opt.isPresent()) {
                TokenHistory tokenHistory = opt.get();
                tokenHistory.setRefreshCount(tokenHistory.getRefreshCount() + 1);
                tokenHistoryRepository.save(tokenHistory);

                log.info(" Token 업데이트 및 재발급 완료");

                //  새로운 객체를 생성하여 안전하게 반환
                resp.setHeader("Authorization","Bearer "+newAccessToken);
                UserDTO accessUser = UserDTO.loginUser(user);
                //user.setPassword("");

                return ResponseDTO.success(ResponseCode.LOGIN_REFRESH_TOKEN_SUCCESS,accessUser);

            }

            return ResponseDTO.error(ResponseCode.LOGIN_REFRESH_TOKEN_NOT_FOUND);

        } catch (Exception e) {
            log.info("❌ Refresh Token 처리 중 오류 발생: {}", e.getMessage());
            throw new CustomException(ResponseCode.LOGIN_INTERNAL_SERVER_ERROR);
        }
    }


    //회원가입

    /**
     * @param userDTO
     * @return
     */
    public ResponseDTO insertUser(UserDTO userDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encodedPassword);
            User savedUser = userDTO.register();
            userMapper.insertUser(savedUser);
            long id = savedUser.getId();

            return ResponseDTO.success(ResponseCode.MEMBER_REGISTER_SUCCESS);
        } catch (Exception e) {
            log.error(e);
            throw new CustomException(ResponseCode.MEMBER_REGISTER_FAIL);
        }

    }


    //회원가입 인증절차

    /**
     * @param type
     * @param value
     * @return
     */
    public boolean registerValidation(String type, String value) {
        Optional<User> opt = Optional.empty();
        switch (type) {
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
                log.info("유효하지 않은 타입 : {}", type);
                break;
        }

        if (opt.isPresent()) {
            //사용할 수 없는 값
            log.info("이미 존재하는 데이터입니다.");
            return false;
        } else {
            log.info("사용가능한 데이터입니다.");
            return true;
        }

    }


    /**
     *
     * @param tokenHistory
     */
    public void revokeTokensByRefreshToken(TokenHistory tokenHistory) {
        tokenHistory.setRevoked(true);
        tokenHistoryRepository.save(tokenHistory);
//        Query query = new Query();
//        query.addCriteria(Criteria.where("refreshToken").is(refreshToken)); //  특정 userId 찾기
//        Update update = new Update();
//        update.set("revoked", true); //  revoke = true로 설정
//        mongoTemplate.updateFirst(query, update, TokenHistory.class); //  하나의 문서만 업데이트
    }


    //  userId를 기준으로 모든 Refresh Token을 revoke = true로 변경

    @Transactional
    public void revokeAllTokensByUserId(String userId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("hashed_userid").is(userId)); //  특정 userId 찾기
        Update update = new Update();
        update.set("revoked", true); // revoke = true로 설정

        try {
            mongoTemplate.updateMulti(query, update, TokenHistory.class); // 여러 개 업데이트

        } catch (Exception e) {
            log.error("Refresh Token 무효화 중 오류 발생 - userId: {}", userId, e);
            throw new CustomException(ResponseCode.LOGIN_REFRESH_TOKEN_INTERNAL_ERROR);
        }
    }

    public String getIdWithRefreshToken(String refresh) {
        if (refresh == null) {
            return null;
        }

        log.info("uid 확인 ! {}", jwtTokenProvider.getUid(refresh));
        return jwtTokenProvider.getUid(refresh);
    }


    public ResponseDTO updateByUserForWithdrawal(long id,String refreshToken,HttpServletResponse response){
        log.info("탈퇴로직 시작");
        try{
            boolean isExist = userMapper.userIsExist(id);
            if(!isExist){
                return ResponseDTO.error(ResponseCode.MEMBER_DELETE_FAIL);
            }

            int result = userMapper.updateUserWithdrawal(id, LocalDateTime.now(),"DeActive");
            if(result != 1){
                return ResponseDTO.error(ResponseCode.MEMBER_DELETE_FAIL);
            }

            // 로그아웃 처리,
            String encrypted = TokenEncrpytor.encrypt(refreshToken);
            Optional<TokenHistory> opt = tokenHistoryRepository.findByRefreshToken(encrypted);
            if (opt.isPresent()) {
                TokenHistory tokenHistory = opt.get();
                revokeTokensByRefreshToken(tokenHistory);

            }
            // Refresh Token 삭제를 위한 빈 쿠키 설정
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(0) // 즉시 삭제
                    .build();

            response.addHeader("Set-Cookie", deleteCookie.toString());
            //
            return ResponseDTO.success(ResponseCode.MEMBER_DELETE_SUCCESS);
        }catch (Exception e){
            log.error(e);
            throw new CustomException(ResponseCode.MEMBER_DELETE_FAIL);
        }

    }
}
