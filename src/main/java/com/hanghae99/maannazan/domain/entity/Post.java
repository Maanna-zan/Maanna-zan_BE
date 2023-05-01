package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

//@Where(clause = "deleted_at IS NULL")
//@SQLDelete(sql = "UPDATE POST SET deleted_at = CONVERT_TZ(now(), 'UTC', 'Asia/Seoul') WHERE id = ?")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    @Size(min = 1, message = "제목을 입력해주세요.")
    private String title;
    @Lob
    @Size(min = 1, message = "내용을 입력해주세요.")
    private String description;
    @Column
    private int likecnt;

    @Column(length = 200)
    private String s3Url;
    @Column(length = 200)
    private String fileName;
    @Column
    private int viewCount;
    @Column
    private double taste;
    @Column
    private double service;
    @Column
    private double atmosphere;
    @Column
    private double satisfaction;



    // 게시글에 위도 경도가 있어야하는게 좀 이상한것 같다  위치나 술집에 대한 table 있어야 할듯.
    //  ManyToOne으로 술집 공공데이터에 연결하는 방법 찾아봐야할듯
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @JoinColumn(name = "KAKAO_ID")
    @ManyToOne
    private Kakao kakao;





    public Post(Kakao kakao, PostRequestDto postRequestDto, User user) {

        entityErrorList(kakao,postRequestDto,user);
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.user = user;
        this.s3Url = postRequestDto.getS3Url();
        this.fileName = postRequestDto.getFileName();
        this.kakao = kakao;
        this.taste = postRequestDto.getTaste();
        this.service = postRequestDto.getService();
        this.atmosphere = postRequestDto.getAtmosphere();
        this.satisfaction = postRequestDto.getSatisfaction();
    }

    public Post(PostRequestDto postRequestDto, User user) {

        entityErrorList(postRequestDto, user);
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.user = user;
        this.s3Url = postRequestDto.getS3Url();
        this.fileName = postRequestDto.getFileName();
        this.taste = postRequestDto.getTaste();
        this.service = postRequestDto.getService();
        this.atmosphere = postRequestDto.getAtmosphere();
        this.satisfaction = postRequestDto.getSatisfaction();

    }

    public void update(PostRequestDto postRequestDto, User user) {
        entityErrorList(postRequestDto,user);
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.s3Url = postRequestDto.getS3Url();
        this.user = user;
        this.fileName = postRequestDto.getFileName();
        this.taste = postRequestDto.getTaste();
        this.service = postRequestDto.getService();
        this.atmosphere = postRequestDto.getAtmosphere();
        this.satisfaction = postRequestDto.getSatisfaction();

    }


    public Post(PostRequestDto postRequestDto) {      //S3Controller save 메서드 (없어도 되긴 함)
        entityErrorList(postRequestDto);
        this.title = postRequestDto.getTitle();
        this.description = postRequestDto.getDescription();
        this.s3Url = postRequestDto.getS3Url();
        this.fileName = postRequestDto.getFileName();
        this.taste = postRequestDto.getTaste();
        this.service = postRequestDto.getService();
        this.atmosphere = postRequestDto.getAtmosphere();
        this.satisfaction = postRequestDto.getSatisfaction();
    }

    public void likeCount(int plusOrMinus) {  //좋아요 개수
        this.likecnt = plusOrMinus;
    }

    public void viewCount(int plusCount) {  //조회수
        this.viewCount = plusCount;
    }

    public void entityErrorList(Kakao kakao, PostRequestDto postRequestDto, User user){ //컬럼마다 예외처리
        if(postRequestDto.getTitle()==null||postRequestDto.getTitle().isEmpty()){
            throw new CustomException(CustomErrorCode.TITLE_IS_EMPTY);
        }
        if(postRequestDto.getDescription()==null||postRequestDto.getDescription().isEmpty()){
            throw new CustomException(CustomErrorCode.CONTENT_IS_EMPTY);
        }
        if(user.getId()<=0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        if(kakao.getApiId()==null || kakao.getApiId().isEmpty()){
            throw new CustomException(CustomErrorCode.FALSE_API_ID);
        }
        if(postRequestDto.getTaste()<1 || postRequestDto.getTaste()>5){
            throw new CustomException(CustomErrorCode.TASTE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getService()<1 || postRequestDto.getService()>5){
            throw new CustomException(CustomErrorCode.SERVICE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getAtmosphere()<1 || postRequestDto.getAtmosphere()>5){
            throw new CustomException(CustomErrorCode.ATMOSPHERE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getSatisfaction()<1 || postRequestDto.getSatisfaction()>5){
            throw new CustomException(CustomErrorCode.SATISFACTION_VALUE_IS_FALSE);
        }
    }

    public void entityErrorList(PostRequestDto postRequestDto, User user){ //컬럼마다 예외처리
        if(postRequestDto.getTitle()==null||postRequestDto.getTitle().isEmpty()){
            throw new CustomException(CustomErrorCode.TITLE_IS_EMPTY);
        }
        if(postRequestDto.getDescription()==null||postRequestDto.getDescription().isEmpty()){
            throw new CustomException(CustomErrorCode.CONTENT_IS_EMPTY);
        }
        if(user.getId()<=0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        if(postRequestDto.getTaste()<1 || postRequestDto.getTaste()>5){
            throw new CustomException(CustomErrorCode.TASTE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getService()<1 || postRequestDto.getService()>5){
            throw new CustomException(CustomErrorCode.SERVICE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getAtmosphere()<1 || postRequestDto.getAtmosphere()>5){
            throw new CustomException(CustomErrorCode.ATMOSPHERE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getSatisfaction()<1 || postRequestDto.getSatisfaction()>5){
            throw new CustomException(CustomErrorCode.SATISFACTION_VALUE_IS_FALSE);
        }
    }

    public void entityErrorList(PostRequestDto postRequestDto){ //컬럼마다 예외처리
        if(postRequestDto.getTitle()==null||postRequestDto.getTitle().isEmpty()){
            throw new CustomException(CustomErrorCode.TITLE_IS_EMPTY);
        }
        if(postRequestDto.getDescription()==null||postRequestDto.getDescription().isEmpty()){
            throw new CustomException(CustomErrorCode.CONTENT_IS_EMPTY);
        }
        if(postRequestDto.getTaste()<1 || postRequestDto.getTaste()>5){
            throw new CustomException(CustomErrorCode.TASTE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getService()<1 || postRequestDto.getService()>5){
            throw new CustomException(CustomErrorCode.SERVICE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getAtmosphere()<1 || postRequestDto.getAtmosphere()>5){
            throw new CustomException(CustomErrorCode.ATMOSPHERE_VALUE_IS_FALSE);
        }
        if(postRequestDto.getSatisfaction()<1 || postRequestDto.getSatisfaction()>5){
            throw new CustomException(CustomErrorCode.SATISFACTION_VALUE_IS_FALSE);
        }
    }
}