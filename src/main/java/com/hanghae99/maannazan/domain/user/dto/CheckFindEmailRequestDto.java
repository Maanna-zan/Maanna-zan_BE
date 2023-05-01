package com.hanghae99.maannazan.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckFindEmailRequestDto {
    @NotBlank(message = "이름은 필수사항 입니다.")
    @Size(min = 2, max = 5, message = "사용자 이름은 최소 2글자 최대 5글자로 구성되어야합니다.")
    private String userName;

    @NotBlank(message = "휴대폰번호는 필수사항 입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)([0-9]{3,4})([0-9]{4})$", message = "휴대폰번호 형식에 맞지 않습니다.")
    private String phoneNumber;
}
