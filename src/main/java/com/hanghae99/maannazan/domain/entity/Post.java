package com.hanghae99.maannazan.domain.entity;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storename;

    private String title;

    private String description;

    private int likecnt;

    @Column
    private String s3Url;

    private String fileName;

    private String apiId;

    private String placeName;

    private int viewCount;



    // 게시글에 위도 경도가 있어야하는게 좀 이상한것 같다  위치나 술집에 대한 table 있어야 할듯.
    //  ManyToOne으로 술집 공공데이터에 연결하는 방법 찾아봐야할듯
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @JoinColumn(name = "KAKAO_ID")
    @ManyToOne
    private Kakao kakao;





    public Post(PostRequestDto postRequestDto, User user) {
        this.storename = postRequestDto.getStorename();
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.user = user;
        this.s3Url = postRequestDto.getS3Url();
        this.fileName = postRequestDto.getFileName();
        this.apiId = postRequestDto.getApiId();
    }

    public void update(PostRequestDto postRequestDto, User user) {
        this.storename = postRequestDto.getStorename();
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.s3Url = postRequestDto.getS3Url();
        this.user = user;
        this.fileName = postRequestDto.getFileName();
        this.apiId = postRequestDto.getApiId();

    }


    public Post(PostRequestDto postRequestDto) {      //S3Controller save 메서드 (없어도 되긴 함)
        this.storename = postRequestDto.getStorename();
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.s3Url = postRequestDto.getS3Url();
        this.fileName = postRequestDto.getFileName();
        this.apiId = postRequestDto.getApiId();

    }

    public void likeCount(int plusOrMinus) {  //좋아요 개수
        this.likecnt = plusOrMinus;
    }

    public void viewCount(int plusCount) {  //좋아요 개수
        this.viewCount = plusCount;
    }

}