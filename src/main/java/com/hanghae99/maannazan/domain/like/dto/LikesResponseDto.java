package com.hanghae99.maannazan.domain.like.dto;

import com.hanghae99.maannazan.domain.entity.Likes;
import lombok.Getter;

@Getter
public class LikesResponseDto {
    private Long id;
    private String apiId;

    public LikesResponseDto(Likes likes) {
        this.id = likes.getId();
        this.apiId = likes.getKakao().getApiId();
    }
}
