package com.hanghae99.maannazan.domain.post.dto;

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
    private String fileTitle;
    private String s3Url;
    private MultipartFile file;
    private String fileName;  //이걸로 수정 삭제


}
