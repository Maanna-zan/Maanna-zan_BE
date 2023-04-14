package com.hanghae99.maannazan.global.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");
        if(accessToken != null) {
            if(!jwtUtil.validateToken(accessToken)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            setAuthentication(jwtUtil.getUserInfoFromToken(accessToken));
        }
        // 어세스 토큰이 만료된 상황 && 리프레시 토큰 또한 존재하는 상황
        else if (refreshToken != null) {
            // 리프레시 토큰 검증 && 리프레시 토큰 DB에서  토큰 존재유무 확인
            boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
            // 리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을때 똑같다면
            if (isRefreshToken) {
                // 리프레시 토큰으로 아이디 정보 가져오기
                String loginId = jwtUtil.getUserInfoFromToken(refreshToken);
                // 새로운 어세스 토큰 발급
                String newAccessToken = jwtUtil.createToken(loginId, "Access");
                // 헤더에 어세스 토큰 추가
                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                // Security context에 인증 정보 넣기
                setAuthentication(jwtUtil.getUserInfoFromToken(newAccessToken));
            }
            // 리프레시 토큰이 만료 || 리프레시 토큰이 DB와 비교했을때 똑같지 않다면
            else {
                jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST.value());
                return;
            }
        }
        filterChain.doFilter(request,response);
    }


    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new ResponseMessage<>(msg, statusCode, ""));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
