package com.hanghae99.maannazan.domain.kakaologin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        kakaoService.kakaoLogin(code, response);

        // 세션에 액세스 토큰과 리프레시 토큰 저장

        return "home";
    }

    @GetMapping("/OAuth/Kakao/{refreshToken}")
    public String getRefresh(@PathVariable String refreshToken, HttpServletResponse response) throws JsonProcessingException {

      kakaoService.getRefresh(refreshToken, response);


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


