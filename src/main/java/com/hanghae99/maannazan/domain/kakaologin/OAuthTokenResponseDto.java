package com.hanghae99.maannazan.domain.kakaologin;

import lombok.Data;

@Data
public class OAuthTokenResponseDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}
