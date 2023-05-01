package com.hanghae99.maannazan.domain.entity;


import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Search{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(min = 1, message = "검색어를 입력해주세요.")
    private String searchWord;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Search(String searchWord, User user) {

        if(user.getId()==null || user.getId()<0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        this.searchWord = searchWord;
        this.user = user;
    }


}