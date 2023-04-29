package com.hanghae99.maannazan.domain.kakaoapi.dto;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.Likes;
import com.hanghae99.maannazan.domain.post.dto.PostImageResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    private List<PostImageResponseDto> postList = new ArrayList<>();



    public AlkolResponseDto(Kakao kakao,boolean roomLike, Likes likes, List<PostImageResponseDto> postImageResponseDtoList){
        this.apiId = kakao.getApiId();
        this.address_name = kakao.getAddressName();
        this.category_group_code = kakao.getCategoryGroupCode();
        this.category_group_name = kakao.getCategoryGroupName();
        this.category_name = kakao.getCategoryName();
        this.distance = kakao.getDistance();
        this.phone = kakao.getPhone();
        this.place_name = kakao.getPlaceName();
        this.place_url = kakao.getPlaceUrl();
        this.road_address_name = kakao.getRoadAddressName();
        this.x = kakao.getX();
        this.y = kakao.getY();
        this.roomViewCount = kakao.getRoomViewCount();
        this.roomLikecnt = kakao.getRoomLikecnt();
        this.postList = postImageResponseDtoList;
        this.roomLike = roomLike;
    }

    public AlkolResponseDto(Kakao kakao, boolean roomLike, List<PostImageResponseDto> postImageResponseDtoList){
        this.apiId = kakao.getApiId();
        this.address_name = kakao.getAddressName();
        this.category_group_code = kakao.getCategoryGroupCode();
        this.category_group_name = kakao.getCategoryGroupName();
        this.category_name = kakao.getCategoryName();
        this.distance = kakao.getDistance();
        this.phone = kakao.getPhone();
        this.place_name = kakao.getPlaceName();
        this.place_url = kakao.getPlaceUrl();
        this.road_address_name = kakao.getRoadAddressName();
        this.x = kakao.getX();
        this.y = kakao.getY();
        this.roomViewCount = kakao.getRoomViewCount();
        this.roomLikecnt = kakao.getRoomLikecnt();
        this.roomLike = roomLike;
        this.postList = postImageResponseDtoList;
    }

    public AlkolResponseDto(Kakao kakao, int numberOfPosts, boolean roomLike, List<PostImageResponseDto> postImageResponseDtoList){
        this.apiId = kakao.getApiId();
        this.address_name = kakao.getAddressName();
        this.category_group_code = kakao.getCategoryGroupCode();
        this.category_group_name = kakao.getCategoryGroupName();
        this.category_name = kakao.getCategoryName();
        this.distance = kakao.getDistance();
        this.phone = kakao.getPhone();
        this.place_name = kakao.getPlaceName();
        this.place_url = kakao.getPlaceUrl();
        this.road_address_name = kakao.getRoadAddressName();
        this.x = kakao.getX();
        this.y = kakao.getY();
        this.numberOfPosts = numberOfPosts;
        this.roomViewCount = kakao.getRoomViewCount();
        this.roomLikecnt = kakao.getRoomLikecnt();
        this.roomLike = roomLike;
        this.postList = postImageResponseDtoList;
    }



}
