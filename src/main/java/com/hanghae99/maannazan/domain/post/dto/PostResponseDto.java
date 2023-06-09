package com.hanghae99.maannazan.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;

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

    private String s3Url;
    private List<CommentResponseDto> commentList = new ArrayList<>();
    private boolean like;
    private int viewCount;

    private double taste;
    private double service;
    private double atmosphere;
    private double satisfaction;

    private double postStarAvg;
    private String categoryName;

    private String storename;
    private String apiId;



    public PostResponseDto(Post post, boolean like, List<CommentResponseDto> commentResponseDtoList) { //산하 게시물 하나 조회 (category가 null이라면 이걸 반환)
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
        this.taste = post.getTaste();
        this.service = post.getService();
        this.atmosphere = post.getAtmosphere();
        this.satisfaction = post.getSatisfaction();
        this.postStarAvg = (taste+service+atmosphere+satisfaction)/4;
        this.apiId = post.getKakao().getApiId();
        this.storename = post.getKakao().getPlaceName();
        this.categoryName = post.getKakao().getCategoryName();
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
        this.storename = post.getKakao().getPlaceName();
        this.apiId = post.getKakao().getApiId();
        this.commentList = commentResponseDtoList;
        this.viewCount = post.getViewCount();
        this.taste = post.getTaste();
        this.service = post.getService();
        this.atmosphere = post.getAtmosphere();
        this.satisfaction = post.getSatisfaction();
        this.postStarAvg = (taste+service+atmosphere+satisfaction)/4;
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
        this.storename = post.getKakao().getPlaceName();
        this.apiId = post.getKakao().getApiId();
        this.viewCount = post.getViewCount();
        this.taste = post.getTaste();
        this.service = post.getService();
        this.atmosphere = post.getAtmosphere();
        this.satisfaction = post.getSatisfaction();
        this.postStarAvg = (taste+service+atmosphere+satisfaction)/4;
    }
    public PostResponseDto(Post post, boolean like){
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.nickname = post.getUser().getNickName();
        this.modifiedAt = post.getModifiedAt();
        this.createAt = post.getCreatedAt();
        this.s3Url = post.getS3Url();
        this.storename = post.getKakao().getPlaceName();
        this.apiId = post.getKakao().getApiId();
        this.likecnt = post.getLikecnt();
        this.like = like;
        this.viewCount = post.getViewCount();
        this.taste = post.getTaste();
        this.service = post.getService();
        this.atmosphere = post.getAtmosphere();
        this.satisfaction = post.getSatisfaction();
        this.postStarAvg = (taste+service+atmosphere+satisfaction)/4;
    }

}


