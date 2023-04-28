package com.hanghae99.maannazan.domain.comment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글을 입력해주세요.")// size랑 동시에 써도 되는지?
    @Size(min = 1, max = 100, message = "댓글은 최소 1자에서 최대 100자 이내여야 합니다.")
    private String content;

    public CommentRequestDto(String content){
        this. content = content;
    }
}