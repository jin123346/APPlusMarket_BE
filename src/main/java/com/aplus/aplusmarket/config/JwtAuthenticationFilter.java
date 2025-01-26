package com.aplus.aplusmarket.config;


import com.nimbusds.oauth2.sdk.auth.JWTAuthenticationClaimsSet;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        try{
            if(token != null && jwtTokenProvider.validateToken(token)) {
                log.info("토큰 잘 뽑힌다. {}",token);
                String uid = jwtTokenProvider.getUid(token);

                // 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(uid, null, null)
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
}
