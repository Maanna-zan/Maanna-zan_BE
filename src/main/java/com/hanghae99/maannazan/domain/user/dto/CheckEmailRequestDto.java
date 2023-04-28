package com.hanghae99.maannazan.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class CheckEmailRequestDto {
    @NotBlank(message = "email은 필수사항 입니다.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;
}
