package com.hanghae99.maannazan.domain.comment.dto;

import com.hanghae99.maannazan.domain.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {

    private Long id;
    private String nickName;
    private String content;
    private LocalDateTime createdAt;
    private int likecnt;
    private Long parentId;

    private List<CommentResponseDto> commentList = new ArrayList<>();

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.nickName = comment.getUser().getNickName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().withNano(0);
        this.likecnt = comment.getLikecnt();
        for(Comment comments : comment.getChildren()){
            commentList.add(new CommentResponseDto(comments));
        }
    }
    public CommentResponseDto(Comment comment, Long parentId) {
        this.id = comment.getId();
        this.nickName = comment.getUser().getNickName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt().withNano(0);
        this.likecnt = comment.getLikecnt();
        this.parentId = parentId;
        for(Comment comments : comment.getChildren()){
            commentList.add(new CommentResponseDto(comments, comment.getId()));
        }
    }


}
