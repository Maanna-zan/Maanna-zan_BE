package com.hanghae99.maannazan.domain.file.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDto {
    private String title;
    private String s3url;
    private MultipartFile file;     //import가 되네요??
}
