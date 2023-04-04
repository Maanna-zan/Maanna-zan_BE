package com.hanghae99.maannazan.domain.mypage;

import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.mypage.dto.MyPageResponseDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final PostRepository postRepository;
    public MyPageResponseDto getMyPage(User user) {
        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts){
            postResponseDtos.add(new PostResponseDto(post));
        }
        return new MyPageResponseDto(user,postResponseDtos);
    }
}
