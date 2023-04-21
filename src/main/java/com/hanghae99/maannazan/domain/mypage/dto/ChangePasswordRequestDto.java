package com.hanghae99.maannazan.domain.mypage.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Getter
public class ChangePasswordRequestDto {
    @NotBlank(message = "변경할 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 8~20글자, 알파벳, 숫자, 특수문자를 최소 하나씩 입력해야 합니다.")
    private String password;

    private String oldPassword;
}
