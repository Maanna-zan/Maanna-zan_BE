package com.hanghae99.maannazan.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CheckFindPwRequestDto {

    @NotBlank(message = "이메일을 입력해 주세요")
    private String email;

}
