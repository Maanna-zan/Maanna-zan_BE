package com.hanghae99.maannazan.domain.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae99.maannazan.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MyPagePostResponseDto {
    private Long id;
    private String title;

    private String description;

    private int likecnt;

    private String nickname;   //Post 작성자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDateTime modifiedAt;

    private String s3Url;
    private boolean like;
    private int viewCount;

    public MyPagePostResponseDto(Post post, boolean like){
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.s3Url = post.getS3Url();
        this.likecnt = post.getLikecnt();
        this.like = like;
    }


}
