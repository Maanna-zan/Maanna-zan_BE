package com.hanghae99.maannazan.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String description;
    private String image;
    private String nickname;
    private String createdate;
    private String modifidate;
    private boolean likeCheck;
    private int likeCnt;

}