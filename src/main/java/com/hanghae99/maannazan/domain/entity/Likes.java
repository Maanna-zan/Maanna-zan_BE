package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;



    private boolean status;

    public Likes(Post post, User user) {
        if(post.getId()==null || post.getId()<0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        if(user.getId()==null || user.getId()<0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        this.user= user;
        this.post= post;
        this.status = true;
    }



    public Post getPost() {
        return this.post;
    }

}
