package com.hanghae99.maannazan.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Kakao {

    @Id
    private String apiId;
    @Column(name="address_name")
    private String addressName;
    @Column(name="category_group_code")
    private String categoryGroupCode;
    @Column(name="category_group_name")
    private String categoryGroupName;
    @Column(name="category_name")
    private String categoryName;

    private String distance;

    private String phone;
    @Column(name="place_name")
    private String placeName;
    @Column(name="place_url")
    private String placeUrl;
    @Column(name="road_address_name")
    private String roadAddressName;

    private String x;

    private String y;

    private boolean roomLike = false;

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
