package com.hanghae99.maannazan.domain.kakaologin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@Validated
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;
    //modify2
    @GetMapping("/OAuth/Kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String createToken = kakaoService.kakaoLogin(code, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(jwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "deliveryHome";
    }
}

