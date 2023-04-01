package com.hanghae99.maannazan.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String description;
    private String image;

    public PostRequestDto(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.image = postRequestDto.getImage();
    }
}
