package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean soju;
    private boolean beer;
    @JoinColumn(name = "POST_ID")
    @OneToOne
    private Post post;

//    @JoinColumn(name = "ROOM_ID")
//    @OneToOne
//    private Room room;

    public Category(PostRequestDto requestDto, Post post) {
        this.soju = requestDto.isSoju();
        this.beer = requestDto.isBeer();
        this.post = post;
    }
}