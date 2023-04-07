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

    private double x;    //위도

    private double y;    //경도

    @Column
    private String s3Url;




    // 게시글에 위도 경도가 있어야하는게 좀 이상한것 같다  위치나 술집에 대한 table 있어야 할듯.
    //  ManyToOne으로 술집 공공데이터에 연결하는 방법 찾아봐야할듯
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    //Comment테이블은 연관관계의 주인인 post 테이블의 "comment" 필드에 해당한다
    private List<Comment> comments = new ArrayList<>();


    public Post(PostRequestDto postRequestDto, User user) {
        this.storename = postRequestDto.getStorename();
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.x = postRequestDto.getX();
        this.y = postRequestDto.getY();
        this.user = user;
        this.s3Url = postRequestDto.getS3Url();

    }

    public void update(PostRequestDto postRequestDto, User user) {
        this.storename = postRequestDto.getStorename();
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.s3Url = postRequestDto.getS3Url();
        this.user = user;
    }


    public Post(PostRequestDto postRequestDto) {      //S3Controller save 메서드 (없어도 되긴 함)
        this.storename = postRequestDto.getStorename();
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.x = postRequestDto.getX();
        this.y = postRequestDto.getY();
        this.s3Url = postRequestDto.getS3Url();
    }




    public void likeCount(int plusOrMinus) {  //좋아요 개수
        this.likecnt = plusOrMinus;
    }

}