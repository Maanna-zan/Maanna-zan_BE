package com.hanghae99.maannazan.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String image;

    private int likecnt;

    private Boolean check;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    
    private List<String> categoryList;
}