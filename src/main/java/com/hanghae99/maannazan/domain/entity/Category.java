package com.hanghae99.maannazan.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;

@Getter
@NoArgsConstructor
public class Category extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean soju;
    private boolean room;

    @JoinColumn(name = "POST_ID")
    @ManyToOne
    private Post post;

}