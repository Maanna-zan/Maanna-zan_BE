package com.hanghae99.maannazan.domain.mypage.dto;

import com.hanghae99.maannazan.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    private Long id;
    private String userName;
    private String nickName;

    private String email;

    private String phoneNumber;
    private String birth;
    private int postCnt;

    private List<MyPagePostResponseDto> posts;

    public MyPageResponseDto(User user, List<MyPagePostResponseDto> posts) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.birth = user.getBirth();
        this.postCnt = posts.size();
        this.posts = posts;
    }

}
