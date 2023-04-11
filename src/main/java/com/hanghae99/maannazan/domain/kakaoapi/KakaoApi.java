package com.hanghae99.maannazan.domain.kakaoapi;

import com.hanghae99.maannazan.domain.post.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class KakaoApi {
    @Value("${kakao.key}")
    private String key;
    private String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

    private PostService postService;



    @GetMapping("/kakaoApi")
    public Map callApi(
            @RequestParam(required = false) String y,
            @RequestParam(required = false) String x,
            @RequestParam String query,
            @RequestParam Integer radius,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String sort
            ){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + key);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI targetUrl = UriComponentsBuilder
                .fromUriString(url) //기본 url
                .queryParam("query", query)
                .queryParam("y", y)//인자
                .queryParam("x", x)//인자
                .queryParam("radius", radius)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort)
                .build()
                .encode(StandardCharsets.UTF_8) //인코딩
                .toUri();
        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, Map.class);
        return result.getBody(); //내용 반환
    }
}
