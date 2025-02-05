package com.aplus.aplusmarket.config;


import com.aplus.aplusmarket.dto.auth.requset.AuthenticationDTO;
import com.aplus.aplusmarket.util.MyUserDetails;
import com.aplus.aplusmarket.util.MyUserDetailsService;
import com.nimbusds.oauth2.sdk.auth.JWTAuthenticationClaimsSet;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
    2025.1.26 하진희 jwt 필터 구성
*/

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        try{
            if(token != null && jwtTokenProvider.validateToken(token)) {
                log.info("토큰 잘 뽑힌다. {}",token);
                String uid = jwtTokenProvider.getUid(token);
                AuthenticationDTO auth = authenticationWithToken(token);

                if(auth.getUid() == null){
                    log.error("JWT 토큰 decode 문제");
                    return;
                }
                request.setAttribute("uid",auth.getUid());
                request.setAttribute("id",auth.getId());
                request.setAttribute("nickName",auth.getNickName());

                // 인증 정보 설정
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(uid);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                );
            }

        }catch (Exception e) {
            // 예외 처리 (로그 기록 및 응답 설정)
            log.error("JWT 검증 실패: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
            return; // 필터 체인 중단
        }
        filterChain.doFilter(request, response); // 다음 필터 실행
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private AuthenticationDTO authenticationWithToken(String token){
        Claims claims = jwtTokenProvider.getClaims(token);
        String uid =claims.getSubject();
        long id = claims.get("id",Long.class);
        String nickName = claims.get("nickName",String.class);
        log.info("토큰에서 추출한 id : {}, uid : {} , nickNAme : {}",id,uid,nickName);

        if(uid == null || id == 0L ){
            log.error("토큰에 오류가 발생했습니다.");
            return AuthenticationDTO.builder()
                    .id(0L)
                    .nickName(null)
                    .uid(null)
                    .build();
        }

        return AuthenticationDTO.builder()
                .id(id)
                .uid(uid)
                .nickName(nickName)
                .build();
    }

}
