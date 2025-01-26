package com.aplus.aplusmarket.config;


import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

/*
    2024.01.26 하진희 - jwt 토근제공컴포넌트 생성하기
 */

@Log4j2
@Component
public class JwtTokenProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long validityInMilliseconds = 3600000; //1시간
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 ; // 1일

    /**
     * 토큰 생성 메서드
     * @param id 
     * @param nickName
     * @return 토큰
     */
    public String createToken(long id, String uid,String nickName) {

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("id", id);
        claims.put("nickName", nickName);
        Date now = new Date();
        Date expiry  = new Date(now.getTime() + validityInMilliseconds);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String uid,String nickName) {

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("nickName", nickName);
        Date now = new Date();
        Date expiry  = new Date(now.getTime() + refreshTokenValidity);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
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
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("nickname", String.class); // 닉네임 가져오기
    }

    public String getUid(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                log.info("Token is expired");
                return false; // 토큰 만료
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






}
