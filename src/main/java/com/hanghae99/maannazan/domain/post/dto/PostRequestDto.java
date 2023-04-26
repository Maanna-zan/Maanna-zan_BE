package com.hanghae99.maannazan.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String storename;   //술집명
    private String title;      // 제목
    private String description;
    private boolean soju;
    private boolean beer;
    private String fileTitle;
    private String s3Url;
    private MultipartFile file;
    private String fileName;  //이걸로 수정 삭제
    @Size(min = 1, max = 5, message = "맛의 별점은 0 ~ 5점")
    private double taste;
    @Size(min = 1, max = 5, message = "서비스의 별점은 0 ~ 5점")
    private double service;
    @Size(min = 1, max = 5, message = "분위기의 별점은 0 ~ 5점")
    private double atmosphere;
    @Size(min = 1, max = 5, message = "만족도의 별점은 0 ~ 5점")
    private double satisfaction;


}
