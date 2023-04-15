package com.hanghae99.maannazan.domain.kakaoapi;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.kakaoapi.dto.AlkolResponseDto;
import com.hanghae99.maannazan.domain.kakaoapi.dto.KakaoResponseDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.KakaoApiRepository;
import com.hanghae99.maannazan.domain.repository.LikeRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public List<KakaoResponseDto> getAlkol(String apiId, User user) {
        Kakao kakaoView = kakaoApiRepository.findByApiId(apiId).orElseThrow(() -> new CustomException(CustomErrorCode.ALKOL_NOT_FOUND));
        kakaoView.roomViewCount(kakaoView.getRoomViewCount()+1);
        List<Kakao> kakaos = kakaoApiRepository.findAllByApiId(apiId);
        List<KakaoResponseDto> kakaoResponseDtoList = new ArrayList<>();
        boolean roomLike = likeRepository.existsByKakaoApiIdAndUser(apiId, user);
        for (Kakao kakao : kakaos){
            List<Post> posts = postRepository.findByKakaoApiId(kakao.getApiId());
            int numberOfPosts = posts.size();
            List<PostResponseDto> postResponseDtoList = new ArrayList<>();
            for (Post post : posts){
                if(user!=null) {
                    boolean like = likeRepository.existsByPostIdAndUser(post.getId(), user);
                    postResponseDtoList.add(new PostResponseDto(post, like));

                }else {
                    postResponseDtoList.add(new PostResponseDto(post));
                }
            }
            kakaoResponseDtoList.add(new KakaoResponseDto(kakao, postResponseDtoList,numberOfPosts, roomLike));
        }return kakaoResponseDtoList;

    }

//    게시물 많은 술집 리스트
    @Transactional
    public List<AlkolResponseDto> getBestAlkol(User user, int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "numberOfPosts"));
        Page<Kakao> entityPage = kakaoApiRepository.findAll(pageable);
        List<Kakao> entityList = entityPage.getContent();
        List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
        for (Kakao kakao : entityList){
            List<Post> posts = postRepository.findByKakaoApiId(kakao.getApiId());
            int numberOfPosts = posts.size();
            boolean roomLike = likeRepository.existsByKakaoApiIdAndUser(kakao.getApiId(), user);
//            List<Post> posts = postRepository.findByApiId(kakao.getApiId());
//            int numberOfPosts = posts.size();
            AlkolResponseDtoList.add(new AlkolResponseDto(kakao, numberOfPosts, roomLike));
        }
        return AlkolResponseDtoList;
    }

    public List<AlkolResponseDto> getAllAlkol(User user, int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Kakao> entityPage = kakaoApiRepository.findAll(pageable);
        List<Kakao> entityList = entityPage.getContent();
        List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
        for (Kakao kakao : entityList){
            boolean roomLike = likeRepository.existsByKakaoApiIdAndUser(kakao.getApiId(), user);
            AlkolResponseDtoList.add(new AlkolResponseDto(kakao, roomLike));
        }
        return AlkolResponseDtoList;
    }

}
