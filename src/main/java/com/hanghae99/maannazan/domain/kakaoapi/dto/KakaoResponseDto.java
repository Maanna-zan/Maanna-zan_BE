package com.hanghae99.maannazan.domain.kakaoapi.dto;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoResponseDto {
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

    private List<PostResponseDto> postList = new ArrayList<>();
    private int numberOfPosts;
    private int roomViewCount;
    private int roomLikecnt;
    private boolean roomLike;

    private double tasteAvg;
    private double serviceAvg;
    private double atmosphereAvg;
    private double satisfactionAvg;

    private double starAvg;



    public KakaoResponseDto(Kakao kakao, List<PostResponseDto> postResponseDtoList, int numberOfPosts, boolean roomLike, double tasteAvg, double serviceAvg, double atmosphereAvg, double satisfactionAvg){
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
        this.postList = postResponseDtoList;
        this.numberOfPosts = numberOfPosts;
        this.roomViewCount = kakao.getRoomViewCount();
        this.roomLikecnt = kakao.getRoomLikecnt();
        this.roomLike = roomLike;
        this.tasteAvg = tasteAvg;
        this.serviceAvg = serviceAvg;
        this.atmosphereAvg = atmosphereAvg;
        this.satisfactionAvg = satisfactionAvg;
        this.starAvg = (tasteAvg+serviceAvg+atmosphereAvg+satisfactionAvg)/4;
    }


}
