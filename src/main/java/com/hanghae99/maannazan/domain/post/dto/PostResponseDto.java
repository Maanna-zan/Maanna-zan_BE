package com.hanghae99.maannazan.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;
import com.hanghae99.maannazan.domain.entity.Category;
import com.hanghae99.maannazan.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;

    private String title;

    private String description;

    private int likecnt;

    private String nickname;   //Post 작성자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDateTime modifiedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDateTime createAt;

    private boolean soju;
    private boolean beer;

    private String s3Url;
    private List<CommentResponseDto> commentList = new ArrayList<>();
    private boolean like;
    private int viewCount;




    public PostResponseDto(Category category,boolean like, List<CommentResponseDto> commentResponseDtoList) {   //산하  게시물 하나 조회
        this.id = category.getPost().getId();
        this.title = category.getPost().getTitle();
        this.description = category.getPost().getDescription();
        this.likecnt = category.getPost().getLikecnt();
        this.nickname = category.getPost().getUser().getNickName();
        this.modifiedAt = category.getPost().getModifiedAt();
        this.createAt = category.getPost().getCreatedAt();
        this.soju = category.isSoju();
        this.beer = category.isBeer();
        this.s3Url = category.getPost().getS3Url();
        this.viewCount = category.getPost().getViewCount();
        this.like = like;
        this.commentList = commentResponseDtoList;
        }

    public PostResponseDto(Post post, boolean like, List<CommentResponseDto> commentResponseDtoList) {    //산하 게시물 하나 조회 (category가 null이라면 이걸 반환)
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.likecnt = post.getLikecnt();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.createAt = post.getCreatedAt();
        this.like = like;
        this.s3Url = post.getS3Url();
        this.commentList = commentResponseDtoList;
        this.viewCount = post.getViewCount();
    }
    public PostResponseDto(Category category, List<CommentResponseDto> commentResponseDtoList) {    //산하  게시물 하나 조회 (category가 null이라면 이걸 반환)
        this.id = category.getPost().getId();
        this.title = category.getPost().getTitle();
        this.description = category.getPost().getDescription();
        this.likecnt = category.getPost().getLikecnt();
        this.nickname = category.getPost().getUser().getNickName();
        this.modifiedAt = category.getPost().getModifiedAt();
        this.createAt = category.getPost().getCreatedAt();
        this.s3Url = category.getPost().getS3Url();
        this.commentList = commentResponseDtoList;
        this.viewCount = category.getPost().getViewCount();
    }
    public PostResponseDto(Post post, List<CommentResponseDto> commentResponseDtoList) {    //산하  게시물 하나 조회 (category가 null이라면 이걸 반환)
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.likecnt = post.getLikecnt();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.createAt = post.getCreatedAt();
        this.s3Url = post.getS3Url();
        this.commentList = commentResponseDtoList;
        this.viewCount = post.getViewCount();

    }

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.likecnt = post.getLikecnt();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.createAt = post.getCreatedAt();
        this.s3Url = post.getS3Url();
    }
    public PostResponseDto(Post post, boolean like){
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.createAt = post.getCreatedAt();
        this.s3Url = post.getS3Url();
        this.likecnt = post.getLikecnt();
        this.like = like;
    }



}


