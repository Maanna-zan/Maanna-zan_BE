package com.hanghae99.maannazan.domain.kakaologin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;



import javax.servlet.http.HttpServletResponse;


@Controller
@Validated
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;



    @GetMapping("/OAuth/Kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        kakaoService.kakaoLogin(code, response);
        // 세션에 액세스 토큰과 리프레시 토큰 저장
        return "/";
    }

    @GetMapping("/OAuth/Kakao/refresh")
    public String getRefresh(@RequestParam String refreshToken, HttpServletResponse response) throws JsonProcessingException {

      kakaoService.getRefresh(refreshToken, response);

        return "home";

    }
        // 프론트 배포 되면 지울 것들
        @GetMapping("/home")
        public ModelAndView homePage() {
            return new ModelAndView("index");
        }

        @GetMapping("/login-page")
        public ModelAndView loginPage() {
            return new ModelAndView("login");
        }




    }


