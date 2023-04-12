package com.hanghae99.maannazan.domain.kakaologin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@Validated
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;


    @GetMapping("/OAuth/Kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String[] Tokens = kakaoService.kakaoLogin(code, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie accessCookie = new Cookie("Authorization", Tokens[0].substring(7));
        Cookie refreshCookie = new Cookie("RefreshToken", Tokens[1]);

        accessCookie.setPath("/");
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return "home";
    }

    @GetMapping("/OAuth/Kakao")
    public String getRefresh(@RequestParam String refreshToken, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String[] Tokens = kakaoService.getRefresh(refreshToken, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie accessCookie = new Cookie("Authorization", Tokens[0].substring(7));
        Cookie refreshCookie = new Cookie("RefreshToken", Tokens[1]);

        accessCookie.setPath("/");
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return "home";
    }

        @GetMapping("/home")
        public ModelAndView homePage() {
            return new ModelAndView("index");
        }

        @GetMapping("/login-page")
        public ModelAndView loginPage() {
            return new ModelAndView("login");
        }




    }


