package com.hanghae99.maannazan.domain.post.dto;

import com.hanghae99.maannazan.domain.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private double x;
    private double y;
    private String fileTitle;
    private String s3Url;
    private MultipartFile file;


}
