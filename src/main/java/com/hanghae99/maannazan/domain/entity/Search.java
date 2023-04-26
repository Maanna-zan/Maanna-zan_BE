package com.hanghae99.maannazan.domain.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Search{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String searchWord;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Search(String searchWord, User user) {
        this.searchWord = searchWord;
        this.user = user;
    }


}