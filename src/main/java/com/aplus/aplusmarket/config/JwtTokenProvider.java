package com.aplus.aplusmarket.config;

import java.nio.charset.StandardCharsets;
import com.aplus.aplusmarket.documents.TokenHistory;
import com.aplus.aplusmarket.repository.TokenHistoryRepository;
import com.aplus.aplusmarket.util.TokenEncrpytor;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import static io.jsonwebtoken.Jwts.*;


/*
    2024.01.26 하진희 - jwt 토근제공컴포넌트 생성하기
 */
@RequiredArgsConstructor
@Log4j2
@Component
public class JwtTokenProvider {

    @Value("${token.jwt.secret}")
    private String secret;
    @Value("${token.jwt.issuer}")
    private String issuer;

    private Key key;

    private final long validityInMilliseconds = 3600000; //1시간
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000L; // ✅ 7일

    private final TokenHistoryRepository tokenHistoryRepository;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("🔍 Loaded issuer from properties: {}", issuer);

    }


    /**
     * 토큰 생성 메서드
     * @param id 
     * @param nickName
     * @return 토큰
     */
    public String createToken(long id, String uid,String nickName) {

        Claims claims = claims().setSubject(uid);
        claims.put("id", id);
        claims.put("nickName", nickName);
        Date now = new Date();
        Date expiry  = new Date(now.getTime() + validityInMilliseconds);
        log.info("issuer : {}",issuer);
        log.info("디코딩된 토큰 - Issuer: {}", claims.getIssuer());  // ✅ 저장된 issuer 값 확인
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String uid,String nickName) {

        Claims claims = claims().setSubject(uid);
        claims.put("nickName", nickName);
        Date now = new Date();
        Date expiry  = new Date(now.getTime() + refreshTokenValidity);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id",Long.class);// ID 가져오기
    }

    public String getNickname(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("nickname", String.class); // 닉네임 가져오기
    }

    public String getUid(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public  boolean validateToken(String token) {
        try {
            Claims claims =  Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                log.info("Token is expired");
                return false; // 토큰 만료
            }

            String savedIssuer = claims.get("iss",String.class);
            log.info("저장된 issuer!!! "+savedIssuer);

            log.info("issuer 222: {}",issuer);

            if(savedIssuer ==null || !savedIssuer.equals(issuer)){
                log.warn("이 토큰은 발급자가 다릅니다.");
                //TODO: 블랙리스트 처리 로직
                return false;
            }
            return true;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        }catch (JwtException | IllegalArgumentException e) {
            log.error(e);
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }



    public Claims getClaims(String token){
        log.info("getClaims 메서드 호출 토큰 : {}",token);
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (JwtException e) {
            log.error("토큰에 오류가 발생했습니다. {}",token,e);
            throw e;
         // 예외를 던져서 필터에서 처리
        } catch (Exception e) {
            log.error("JWT 처리 중 알 수 없는 오류 발생: {}", token, e);  // 기타 오류 로그
            throw e;  // 예외를 던져서 필터에서 처리
        }
    }

    public Date getTokenExpiry(String token) {
        Claims claims = parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    //리프레쉬 토큰 부분
    @Transactional
    public TokenHistory saveTokenToDB(long userId, String refreshToken, String deviceInfo, HttpServletRequest request) {
        try {
            Date refreshTokenExpiry = getTokenExpiry(refreshToken);
            String encryptedToken = TokenEncrpytor.encrypt(refreshToken);

            //ip 구하기
            String ip = request.getHeader("X-Forwarded-For"); // 프록시 서버가 있는 경우
            if (ip == null || ip.isEmpty()) {
                ip = request.getRemoteAddr(); // 클라이언트의 실제 IP
            }


            TokenHistory tokenHistory = TokenHistory.builder()
                    .refreshToken(encryptedToken)
                    .expiresAt(refreshTokenExpiry)
                    .issuedAt(LocalDateTime.now())
                    .deviceInfo(deviceInfo)
                    .ipAddress(ip)
                    .hashed_userid(TokenEncrpytor.hashUserId(userId))
                    .build();


            return tokenHistoryRepository.save(tokenHistory);

        }catch (Exception e){
            log.error("MongoDB 토큰 저장 실패 - userId: {}, error: {}", userId, e.getMessage());
            return null; // 저장 실패 시 null 반환
        }

    }











}
