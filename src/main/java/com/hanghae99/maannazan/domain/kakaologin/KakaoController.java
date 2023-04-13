package com.hanghae99.maannazan.domain.kakaologin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpSession;

@Controller
@Validated
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;


    @GetMapping("/OAuth/Kakao")
    public String kakaoLogin(@RequestParam String code, HttpSession session) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String[] tokens = kakaoService.kakaoLogin(code, session);

        // 세션에 액세스 토큰과 리프레시 토큰 저장
        session.setAttribute("accessToken", tokens[0]);
        session.setAttribute("refreshToken", tokens[1]);

        return "home";
    }

    @PostMapping("/OAuth/Kakao")
    public String getRefresh(HttpSession session) throws JsonProcessingException {

        String refreshToken = (String) session.getAttribute("refreshToken");

        String[] Tokens = kakaoService.getRefresh(refreshToken, session);


        session.setAttribute("accessToken", Tokens[0]);
        session.setAttribute("refreshToken", Tokens[1]);

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


