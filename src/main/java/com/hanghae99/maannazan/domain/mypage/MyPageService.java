package com.hanghae99.maannazan.domain.mypage;

import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.mypage.dto.ChangeNickNameRequestDto;
import com.hanghae99.maannazan.domain.mypage.dto.ChangePasswordRequestDto;
import com.hanghae99.maannazan.domain.mypage.dto.MyPageResponseDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.domain.repository.UserRepository;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hanghae99.maannazan.global.exception.CustomErrorCode.*;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public MyPageResponseDto getMyPage(User user) {
        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts){
            postResponseDtos.add(new PostResponseDto(post));
        }
        return new MyPageResponseDto(user,postResponseDtos);
    }

    public void changeNickName(User user, ChangeNickNameRequestDto changeNickNameRequestDto) {
        String nickName = changeNickNameRequestDto.getNickName();
        Optional<User> foundNickName = userRepository.findByNickName(nickName);
        if (foundNickName.isPresent()) throw new CustomException(DUPLICATE_NICKNAME);
        user.changeNickName(nickName);
        userRepository.save(user);
    }


    public void changePassword(User user, ChangePasswordRequestDto changePasswordRequestDto) {
        String password = passwordEncoder.encode(changePasswordRequestDto.getPassword());
        user.changePassword(password);
        userRepository.save(user);
    }
}
