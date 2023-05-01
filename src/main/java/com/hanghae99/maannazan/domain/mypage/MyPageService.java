package com.hanghae99.maannazan.domain.mypage;

import com.hanghae99.maannazan.domain.entity.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hanghae99.maannazan.global.exception.CustomErrorCode.DUPLICATE_NICKNAME;
import static com.hanghae99.maannazan.global.exception.CustomErrorCode.NOT_PROPER_INPUTFORM;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PasswordEncoder passwordEncoder;
    private final PostService postService;
    private final UserRepository userRepository;
    private final LikeService likeService;
    private final LikeRepository likeRepository;
    private final KakaoLikeRepository kakaoLikeRepository;

    private final KakaoApiService kakaoApiService;

    //마이페이지 메인 (내가쓴 게시글 + 유저 정보)
    @Transactional
    public MyPageResponseDto getMyPage(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> entityPage = postService.getPostOrderByCreatedAtDesc(user, pageable);
        List<Post> entityList = entityPage.getContent();
        List<MyPagePostResponseDto> myPagePostResponseDtos = new ArrayList<>();
        for (Post post : entityList) {
            boolean like = likeService.getPostLike(post, user);
            myPagePostResponseDtos.add(new MyPagePostResponseDto(post, like));
        }
        return new MyPageResponseDto(user, myPagePostResponseDtos);
    }

    @Transactional
    public MyPageResponseDto getMyPagelikePost(User user, int page, int size) {
        List<Likes> likesList = likeRepository.findAllByStatusAndUser(true,user);
        List<MyPagePostResponseDto> myPagePostResponseDtos = new ArrayList<>();
        for (Likes likes:likesList){
            Pageable pageable = PageRequest.of(page, size);
            Page<Post> entityPage = postService.getPostList(likes.getPost().getId(),pageable);
            List<Post> entityList = entityPage.getContent();
            for (Post post : entityList) {
                boolean like = likeService.getPostLike(post, user);
                    myPagePostResponseDtos.add(new MyPagePostResponseDto(post, like));
            }
        }
        return new MyPageResponseDto(user, myPagePostResponseDtos);
    }

    @Transactional
    public List<AlkolResponseDto> getMyPagelikeAlkol(User user, int page, int size) {
        List<KakaoLikes> kakaoLikesList = kakaoLikeRepository.findAllByStatusAndUser(true,user);
        List<AlkolResponseDto> AlkolResponseDtoList = new ArrayList<>();
        for (KakaoLikes kakaoLikes:kakaoLikesList){
            Pageable pageable = PageRequest.of(page, size);
            Page<Kakao> entityPage = kakaoApiService.getAlkolList(kakaoLikes.getKakao().getApiId(),pageable);
            List<Kakao> entityList = entityPage.getContent();
            for (Kakao kakao : entityList) {
                boolean roomLike = likeService.getAlkolLike(kakao.getApiId(), user);
                List<Post> posts = postService.getPostByKakaoApiId(kakao);
                List<PostImageResponseDto> postImageResponseDtoList = new ArrayList<>();
                for (Post post : posts) {
                    postImageResponseDtoList.add(new PostImageResponseDto(post));
                }
                    AlkolResponseDto dto = new AlkolResponseDto(kakao,roomLike, postImageResponseDtoList);
                    AlkolResponseDtoList.add(dto);
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
