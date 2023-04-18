package com.hanghae99.maannazan.domain.kakaoapi.dto;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlkolResponseDto {

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

    private int numberOfPosts;
    private int roomViewCount;
    private int roomLikecnt;
    private boolean roomLike;


    public AlkolResponseDto(Kakao kakao, boolean roomLike){
        this.apiId = kakao.getApiId();
        this.address_name = kakao.getAddress_name();
        this.category_group_code = kakao.getCategory_group_code();
        this.category_group_name = kakao.getCategory_group_name();
        this.category_name = kakao.getCategory_name();
        this.distance = kakao.getDistance();
        this.phone = kakao.getPhone();
        this.place_name = kakao.getPlace_name();
        this.place_url = kakao.getPlace_url();
        this.road_address_name = kakao.getRoad_address_name();
        this.x = kakao.getX();
        this.y = kakao.getY();
        this.roomViewCount = kakao.getRoomViewCount();
        this.roomLikecnt = kakao.getRoomLikecnt();
        this.roomLike = roomLike;
    }

    public AlkolResponseDto(Kakao kakao, int numberOfPosts, boolean roomLike){
        this.apiId = kakao.getApiId();
        this.address_name = kakao.getAddress_name();
        this.category_group_code = kakao.getCategory_group_code();
        this.category_group_name = kakao.getCategory_group_name();
        this.category_name = kakao.getCategory_name();
        this.distance = kakao.getDistance();
        this.phone = kakao.getPhone();
        this.place_name = kakao.getPlace_name();
        this.place_url = kakao.getPlace_url();
        this.road_address_name = kakao.getRoad_address_name();
        this.x = kakao.getX();
        this.y = kakao.getY();
        this.numberOfPosts = numberOfPosts;
        this.roomViewCount = kakao.getRoomViewCount();
        this.roomLikecnt = kakao.getRoomLikecnt();
        this.roomLike = roomLike;
    }



}