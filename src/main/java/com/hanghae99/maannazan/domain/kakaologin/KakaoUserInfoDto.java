package com.hanghae99.maannazan.domain.kakaologin;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickName;
    private String profile_image;

    public KakaoUserInfoDto(Long id, String nickName, String email, String profile_image) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.profile_image = profile_image;
    }
}