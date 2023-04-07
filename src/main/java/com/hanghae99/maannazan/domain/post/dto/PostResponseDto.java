package com.hanghae99.maannazan.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;
import com.hanghae99.maannazan.domain.entity.*;
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

    private String storename;

    private String title;

    private String description;

    private int likecnt;


    private String nickname;   //Post 작성자

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern="yyyyMMdd")
    private LocalDateTime modifiedAt;

    private boolean soju;
    private boolean beer;

    private double x;
    private double y;

    private String s3Url;
    private List<CommentResponseDto> commentList = new ArrayList<>();
    private boolean like;

    public PostResponseDto(Category category,boolean like) {   // 게시물 하나 조회
        this.id = category.getPost().getId();
        this.storename = category.getPost().getStorename();
        this.title = category.getPost().getTitle();
        this.description = category.getPost().getDescription();
        this.likecnt = category.getPost().getLikecnt();
        this.nickname = category.getPost().getUser().getNickName();
        this.modifiedAt = category.getPost().getModifiedAt();
        this.soju = category.isSoju();
        this.beer = category.isBeer();
        this.x = category.getPost().getX();
        this.y = category.getPost().getY();
        this.s3Url = category.getPost().getS3Url();
        this.like = like;
        /*List<Comment> comments = category.getPost().getCommentList();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : comments) {
                commentList.add(new CommentResponseDto(comment));
            }
            this.commentList = commentList;
        }*/
    }
    public PostResponseDto(Post post) {    // 게시물 하나 조회 (category가 null이라면 이걸 반환)
        this.id = post.getId();
        this.storename = post.getStorename();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.likecnt = post.getLikecnt();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.x = post.getX();
        this.y = post.getY();
        this.s3Url = post.getS3Url();

        /*List<Comment> comments = post.getCommentList();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : comments) {
                commentList.add(new CommentResponseDto(comment));
            }
            this.commentList = commentList;
        }*/
    }


    public PostResponseDto(Post post, boolean like) {    //메인페이지에서 유저 별 좋아요 체크 (전체 조회)
        this.id = post.getId();
        this.storename = post.getStorename();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.likecnt = post.getLikecnt();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.x = post.getX();
        this.y = post.getY();
        this.like = like;
        this.s3Url = post.getS3Url();
        /*List<Comment> comments = post.getCommentList();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : comments) {
                commentList.add(new CommentResponseDto(comment));
            }
            this.commentList = commentList;
        }*/
    }
    }

