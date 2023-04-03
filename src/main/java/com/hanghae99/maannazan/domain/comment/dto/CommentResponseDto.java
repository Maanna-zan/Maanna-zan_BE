package com.hanghae99.maannazan.domain.comment.dto;

import com.hanghae99.maannazan.domain.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private String nickName;
    private String content;
    private LocalDateTime createdAt;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.nickName = comment.getUser().getNickName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().withNano(0);
    }


}
