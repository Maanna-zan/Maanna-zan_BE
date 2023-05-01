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
public class KakaoLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;


    @ManyToOne
    @JoinColumn(name = "KAKAO_ID")
    private Kakao kakao;

    private boolean status;

    public KakaoLikes(Kakao kakao, User user) {
        if (kakao.getApiId() == null || kakao.getApiId().isEmpty()) {
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        if (user.getId() == null || user.getId() < 0) {
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        this.user = user;
        this.kakao = kakao;
        this.status = true;
    }


}
