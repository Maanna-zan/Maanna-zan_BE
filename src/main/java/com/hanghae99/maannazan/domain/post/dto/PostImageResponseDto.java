package com.hanghae99.maannazan.domain.post.dto;

import com.hanghae99.maannazan.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostImageResponseDto {


    private String s3Url;

    public PostImageResponseDto(Post post) {
        this.s3Url = post.getS3Url();
    }
}
