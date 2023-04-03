package com.hanghae99.maannazan.domain.post.dto;

import com.hanghae99.maannazan.domain.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String storename;   //술집명
    private String title;      // 제목
    private String description;
    private String image;
    private int likecnt;
    private boolean checks;
    private boolean soju;
    private boolean beer;
    private double x;
    private double y;


}
