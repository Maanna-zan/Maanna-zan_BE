package com.hanghae99.maannazan.domain.mypage;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.kakaoapi.KakaoApiService;
import com.hanghae99.maannazan.domain.kakaoapi.dto.AlkolResponseDto;
import com.hanghae99.maannazan.domain.like.LikeService;
import com.hanghae99.maannazan.domain.mypage.dto.ChangeNickNameRequestDto;
import com.hanghae99.maannazan.domain.mypage.dto.ChangePasswordRequestDto;
import com.hanghae99.maannazan.domain.mypage.dto.MyPagePostResponseDto;
import com.hanghae99.maannazan.domain.mypage.dto.MyPageResponseDto;
import com.hanghae99.maannazan.domain.post.PostService;
import com.hanghae99.maannazan.domain.post.dto.PostImageResponseDto;
import com.hanghae99.maannazan.domain.repository.*;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hanghae99.maannazan.global.exception.CustomErrorCode.DUPLICATE_NICKNAME;
import static com.hanghae99.maannazan.global.exception.CustomErrorCode.NOT_PROPER_INPUTFORM;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PasswordEncoder passwordEncoder;
    private final PostService postService;
    private final UserRepository userRepository;
    private final LikeService likeService;

    private final KakaoApiService kakaoApiService;

    //마이페이지 메인 (내가쓴 게시글 + 유저 정보)
    @Transactional
    public MyPageResponseDto getMyPage(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Post> entityPage = postService.getPostOrderByCreatedAtDesc(user,pageable);
        List<Post> entityList = entityPage.getContent();
        List<MyPagePostResponseDto> myPagePostResponseDtos = new ArrayList<>();
        for (Post post : entityList){
            boolean like = likeService.getPostLike(post, user);
            myPagePostResponseDtos.add(new MyPagePostResponseDto(post, like));
        }
        return new MyPageResponseDto(user,myPagePostResponseDtos);
    }

    @Transactional
    public MyPageResponseDto getMyPagelikePost(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Post> entityPage = postService.getPostList(pageable);
        List<Post> entityList = entityPage.getContent();
        List<MyPagePostResponseDto> myPagePostResponseDtos = new ArrayList<>();
        for (Post post : entityList){
            boolean like = likeService.getPostLike(post, user);
            if (like){
                myPagePostResponseDtos.add(new MyPagePostResponseDto(post, true));
            }

        }
        return new MyPageResponseDto(user,myPagePostResponseDtos);
    }

    @Transactional
    public List<AlkolResponseDto> getMyPagelikeAlkol(User user, int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Order.desc("roomLike")));
        Page<Kakao> entityPage = kakaoApiService.getAlkolList(pageable);
        List<Kakao> entityList = entityPage.getContent();
        List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
        for (Kakao kakao : entityList){
            boolean roomLike = likeService.getAlkolLike(kakao.getApiId(), user);
            List<Post> posts = postService.getPostByKakaoApiId(kakao);
            List<PostImageResponseDto> postImageResponseDtoList = new ArrayList<>();
            for (Post post : posts){
                postImageResponseDtoList.add(new PostImageResponseDto(post));
            }
            if (roomLike){
                AlkolResponseDtoList.add(new AlkolResponseDto(kakao, true, postImageResponseDtoList));
            }

        }
        return AlkolResponseDtoList;
    }

    @Transactional
    public void changeNickName(User user, ChangeNickNameRequestDto changeNickNameRequestDto) {
        String nickName = changeNickNameRequestDto.getNickName();
        Optional<User> foundNickName = userRepository.findByNickName(nickName);
        if (foundNickName.isPresent()) throw new CustomException(DUPLICATE_NICKNAME);
        user.changeNickName(nickName);
        userRepository.save(user);
    }


    @Transactional
    public void changePassword(User user, ChangePasswordRequestDto changePasswordRequestDto) {
        if(passwordEncoder.matches(changePasswordRequestDto.getOldPassword(),user.getPassword())){
            String password = passwordEncoder.encode(changePasswordRequestDto.getPassword());
            user.changePassword(password);
            userRepository.save(user);
        }else throw new CustomException(NOT_PROPER_INPUTFORM);
    }
}
