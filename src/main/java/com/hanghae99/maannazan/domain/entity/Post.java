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
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storename;

    private String title;

    private String description;

    private String image;

    private int likecnt;

    private boolean checks;

    private double x;    //위도

    private double y;    //경도

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    // 게시글에 위도 경도가 있어야하는게 좀 이상한것 같다  위치나 술집에 대한 table 있어야 할듯.
    //  ManyToOne으로 술집 공공데이터에 연결하는 방법 찾아봐야할듯
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;


    public Post(PostRequestDto postrequestDto, User user) {
        this.storename = postrequestDto.getStorename();
        this.title = postrequestDto.getTitle();
        this.description = postrequestDto.getDescription();
        this.image = postrequestDto.getImage();
        this.likecnt = postrequestDto.getLikecnt();
        this.checks = postrequestDto.isChecks();
        this.x = postrequestDto.getX();
        this.y = postrequestDto.getY();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.storename = requestDto.getStorename();
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.image = requestDto.getImage();
    }
}