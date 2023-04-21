package com.hanghae99.maannazan.domain.mypage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;



@Getter
public class ChangeNickNameRequestDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message =  "닉네임은 영어, 숫자, 한글로 구성되어야하며 최소 2글자 최대 16글자로 구성되어야합니다.")
    private String nickName;

}
