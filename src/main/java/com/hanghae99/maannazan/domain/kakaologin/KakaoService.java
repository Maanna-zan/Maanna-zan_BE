package com.hanghae99.maannazan.domain.kakaologin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.RefreshTokenRepository;
import com.hanghae99.maannazan.domain.repository.UserRepository;
import com.hanghae99.maannazan.global.jwt.JwtUtil;
import com.hanghae99.maannazan.global.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_key;

    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String[] token = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(token[0]);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
//        String createToken = jwtUtil.createToken(kakaoUser.getNickName(), "Access");
//        String refreshToken = jwtUtil.createToken(kakaoUser.getNickName(), "Refresh");
        response.setHeader("Authorization", token[0]);
        response.setHeader("refreshToken", token[1]);
        /*TokenDto tokenDto = jwtUtil.createAllToken(kakaoUser.getEmail());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserEmail(kakaoUser.getEmail());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), kakaoUser.getEmail());
            refreshTokenRepository.save(newToken);
        }
        setHeader(response, tokenDto);*/


    }
    private void setHeader(HttpServletResponse response, TokenDto tokenDto){
        response.addHeader(jwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(jwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String[] getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


            // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_key);
        body.add("redirect_uri", "http://localhost:3000/oauth");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        /*JsonNode jsonNode = objectMapper.readTree(responseBody);
        String accessToken = jsonNode.get("access_token").asText();
        String refreshToken = jsonNode.get("refresh_token").asText();*/
        OAuthTokenResponseDto oAuthTokenResponseDto = null;
        try {
             oAuthTokenResponseDto =objectMapper.readValue(responseBody, OAuthTokenResponseDto.class);
        }catch(JsonMappingException e){
            e.printStackTrace();
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }

        return new String[] {oAuthTokenResponseDto.getAccess_token(), oAuthTokenResponseDto.getRefresh_token()};

    }
    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",  "Bearer " + accessToken);

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("property_keys", "[\"kakao_account.email\", \"properties.nickname\", \"properties.profile_image\"]");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(requestBody, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String profile_image = jsonNode.get("properties")
                .get("profile_image").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email+", "+profile_image);
        return new KakaoUserInfoDto(id, nickname, email, profile_image);
    }
    // 3. 필요시에 회원가입
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)

                .orElse(null);
        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(kakaoUserInfo.getNickName(),kakaoId, encodedPassword, email, kakaoUserInfo.getProfile_image());
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    public void getRefresh(String refreshToken ,HttpServletResponse response) throws JsonProcessingException {
        String[] tokens = getNewAccessToken(refreshToken);
//        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(tokens[0]);
//        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

 /*       String newAccessToken = jwtUtil.createToken(kakaoUser.getNickName(), "Access");
        String newRefreshToken = jwtUtil.createToken(kakaoUser.getNickName(), "Refresh");*/

        response.setHeader("Authorization", tokens[0]);
        response.setHeader("refreshToken", tokens[1]);

    }

    private String[] getNewAccessToken(String refreshToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+refreshToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", client_key);
        body.add("refresh_token", refreshToken);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 새로운 액세스 토큰 파싱
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthTokenResponseDto oAuthTokenResponseDto = null;
        try {
            oAuthTokenResponseDto =objectMapper.readValue(responseBody, OAuthTokenResponseDto.class);
        }catch(JsonMappingException e){
            e.printStackTrace();
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }

        return new String[] {oAuthTokenResponseDto.getAccess_token(), oAuthTokenResponseDto.getRefresh_token()};

    }



}