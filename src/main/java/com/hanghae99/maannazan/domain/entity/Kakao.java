package com.hanghae99.maannazan.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Kakao {

    @Id
    private String apiId;
    private String address_name;

    private String category_group_code;

    private String category_group_name;

    private String category_name;

    private String distance;

    private String phone;

    private String place_name;

    private String place_url;

    private String road_address_name;

    private String x;

    private String y;

    private int roomLikecnt;

    private int roomViewCount;

    private int numberOfPosts;

    public void postCount(int i) {
        this.numberOfPosts = i;
    }


    public void roomViewCount(int plusCount) {  //조회수
        this.roomViewCount = plusCount;
    }

    public void likeCount(int plusOrMinus) {  // 좋아요 count
        this.roomLikecnt = plusOrMinus;
    }
}
