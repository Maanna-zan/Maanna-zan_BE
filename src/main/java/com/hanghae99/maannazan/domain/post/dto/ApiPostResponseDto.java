package com.hanghae99.maannazan.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae99.maannazan.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ApiPostResponseDto {
    private Long id;

    private String storename;

    private String title;

    private String description;

    private String nickname;   //Post 작성자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDateTime modifiedAt;


    private String s3Url;


    private Long apiId;


    public ApiPostResponseDto(Post post) {
        this.id = post.getId();
        this.storename = post.getStorename();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.apiId = post.getApiId();
        this.s3Url = post.getS3Url();
    }
}
