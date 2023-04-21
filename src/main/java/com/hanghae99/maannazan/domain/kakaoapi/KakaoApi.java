package com.hanghae99.maannazan.domain.kakaoapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae99.maannazan.domain.kakaoapi.dto.AlkolResponseDto;
import com.hanghae99.maannazan.domain.kakaoapi.dto.KakaoResponseDto;
import com.hanghae99.maannazan.domain.post.PostService;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class KakaoApi {

    @Value("${kakao.key}")
    private String key;
    private String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

    private final PostService postService;

    private final KakaoApiService kakaoApiService;

    //카카오 검색 api 호출 및 저장
    @Operation(summary = "kakaoApi", description = "카카오 검색 api 및 저장")
    @GetMapping("/kakaoApi")
    public Map callApi(
            @RequestParam(required = false) String y,
            @RequestParam(required = false) String x,
            @RequestParam String query,
            @RequestParam Integer radius,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String sort
            ) throws JsonProcessingException {
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
        kakaoApiService.apiSave(result.getBody());
        return result.getBody(); //내용 반환
    }
    //술집 상세 조회
    @Operation(summary = "alkol", description = "술집 상세 조회")
    @GetMapping("/alkol/{apiId}")
    public List<KakaoResponseDto> getAlkol(@PathVariable String apiId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails==null){
            return kakaoApiService.getAlkol(apiId,null);
        } else
            return kakaoApiService.getAlkol(apiId,userDetails.getUser());
    }
    // 게시글 많은 순으로 술집 조회
    @Operation(summary = "alkolBest", description = "게시글 많은 순으로 술집 조회")
    @GetMapping("/alkol/best")
    public List<AlkolResponseDto>getBestAlkol(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "3") int size, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails==null){
            return kakaoApiService.getBestAlkol(null,page,size);
        } else
            return kakaoApiService.getBestAlkol(userDetails.getUser(), page,size);
    }

    // 모든 술집 조회
    @Operation(summary = "alkolAll", description = "모든 술집 조회")
    @GetMapping("/alkol/all")
    public List<AlkolResponseDto> getAllAlkol(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails==null){
            return kakaoApiService.getAllAlkol(null,page,size);
        } else
            return kakaoApiService.getAllAlkol(userDetails.getUser(), page,size);
    }

    //좋아요 순으로 술집 조회
    @Operation(summary = "alkolLike", description = "좋아요 순으로 술집 조회")
    @GetMapping("/alkol/like")
    public List<AlkolResponseDto> getLikeAlkol(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails==null){
            return kakaoApiService.getLikeAlkol(null,page,size);
        } else
            return kakaoApiService.getLikeAlkol(userDetails.getUser(), page,size);
    }
    //조회수 순으로 술집 조회
    @Operation(summary = "alkolView", description = "조회수 순으로 술집 조회")
    @GetMapping("/alkol/view")
    public List<AlkolResponseDto> getViewAlkol(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "3") int size, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails==null){
            return kakaoApiService.getViewAlkol(null,page,size);
        } else
            return kakaoApiService.getViewAlkol(userDetails.getUser(), page,size);
    }
}
