package com.hanghae99.maannazan.domain.kakaoapi;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.kakaoapi.dto.KakaoResponseDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.KakaoApiRepository;
import com.hanghae99.maannazan.domain.repository.LikeRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KakaoApiService {
    private final KakaoApiRepository kakaoApiRepository;

    private final PostRepository postRepository;

    private final LikeRepository likeRepository;






    //카카오 검색 api 저장
    public void apiSave(Map<String, Object> body) {
        List<Map<String, Object>> documents = (List<Map<String, Object>>) body.get("documents");
        List<Kakao> kakaos = new ArrayList<>();
        for (Map<String, Object> document : documents) {
            Kakao kakao = new Kakao();
            kakao.setAddress_name(document.get("address_name").toString());
            kakao.setCategory_group_code(document.get("category_group_code").toString());
            kakao.setCategory_group_name(document.get("category_group_name").toString());
            kakao.setCategory_name(document.get("category_name").toString());
            kakao.setDistance(document.get("distance").toString());
            kakao.setApiId(document.get("id").toString());
            kakao.setPhone(document.get("phone").toString());
            kakao.setPlace_name(document.get("place_name").toString());
            kakao.setPlace_url(document.get("place_url").toString());
            kakao.setRoad_address_name(document.get("road_address_name").toString());
            kakao.setX(document.get("x").toString());
            kakao.setY(document.get("y").toString());
            Optional<Kakao> foundKakao = kakaoApiRepository.findByApiId(kakao.getApiId());
            if (foundKakao.isPresent()){
                return;
            }else {
                kakaos.add(kakao);
            }
        }
        kakaoApiRepository.saveAll(kakaos);
    }


    //술집 리스트
    public List<KakaoResponseDto> getAlkol(String apiId, User user) {
        List<Kakao> kakaos = kakaoApiRepository.findAllByApiId(apiId);
        List<KakaoResponseDto> kakaoResponseDtoList = new ArrayList<>();
        for (Kakao kakao : kakaos){
            List<Post> posts = postRepository.findByApiId(kakao.getApiId());
            List<PostResponseDto> postResponseDtoList = new ArrayList<>();
            for (Post post : posts){
                if(user!=null) {
                    boolean like = likeRepository.existsByPostIdAndUser(post.getId(), user);
                    postResponseDtoList.add(new PostResponseDto(post, like));

                }else {
                    postResponseDtoList.add(new PostResponseDto(post));
                }
            }
            kakaoResponseDtoList.add(new KakaoResponseDto(kakao, postResponseDtoList));
        }return kakaoResponseDtoList;

    }

    public List<KakaoResponseDto> getBestAlkol(User user){
        List<Kakao> kakaos = kakaoApiRepository.findAll();
        List<KakaoResponseDto> kakaoResponseDtoList = new ArrayList<>();
        for (Kakao kakao : kakaos){
            List<Post> posts = postRepository.findByApiId(kakao.getApiId());
            int numberOfPosts = posts.size();
            List<PostResponseDto> postResponseDtoList = new ArrayList<>();
            for (Post post : posts){
                boolean like = false;
                if (user != null) {
                    like = likeRepository.existsByPostIdAndUser(post.getId(), user);
                }
                postResponseDtoList.add(new PostResponseDto(post, like));
            }
            kakaoResponseDtoList.add(new KakaoResponseDto(kakao, postResponseDtoList, numberOfPosts));
        }
        kakaoResponseDtoList.sort(Comparator.comparingInt(KakaoResponseDto::getNumberOfPosts).reversed());
        return kakaoResponseDtoList;
    }


}
