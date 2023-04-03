package com.hanghae99.maannazan.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class SignupRequestDto {

    @NotBlank(message = "이름은 필수사항 입니다.")
    @Size(min = 2, max = 5, message = "사용자 이름은 최소 2글자 최대 5글자로 구성되어야합니다.")
    private String userName;

    //닉네임 형식
    //2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성해야 하며 / 한글 초성 및 모음은 허가하지 않음
    @NotBlank(message = "닉네임은 필수사항 입니다.")
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message =  "닉네임은 영어, 숫자, 한글로 구성되어야하며 최소 2글자 최대 16글자로 구성되어야합니다.")
    private String nickName;

    //이메일 형식
    //시작을  0~9 사이 숫자 or a-z A-Z 알바펫 아무거나로 시작하고  /  중간에 - _  . 같은 문자가 있을수도 있고 없을수도 있으며 /
    //그 후에 0~9 사이 숫자 or a-z A-Z 알바펫중 하나의 문자가 없거나 연달아 나올수 있으며 /  @ 가 반드시 존재하고  /
    //0-9a-zA-Z 여기서 하나가 있고  /  중간에 - _  . 같은 문자가 있을수도 있고 없을수도 있으며 / 그 후에 0~9 사이 숫자 or a-z A-Z 알바펫중 하나의
    //문자가 없거나 연달아 나올수 있으며 /  반드시  .  이 존재하고  / [a-zA-Z] 의 문자가 2개나 3개가 존재 /   이 모든것은 대소문자 구분안함
    @NotBlank(message = "이메일은 필수사항 입니다.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    //휴대폰번호 형식
    // 시작을 숫자 01로 시작하며 그 후에 0,1,6,7,8,9 중에 하나가 나올수도 있으며 /  숫자 3~4개 이어지고 /
    // 또 하이픈 - 하나 존재할수도 있으며 /  숫자 4개가 이어짐
    @NotBlank(message = "휴대폰번호는 필수사항 입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)([0-9]{3,4})([0-9]{4})$", message = "휴대폰번호 형식에 맞지 않습니다.")
    private String phoneNumber;

    //비밀번호 형식
    // 알파벳은 소문자, 대문자 혼합사용 가능하며 / 숫자, 알파벳, 특수문자는 하나이상씩 사용해야 하며 / 최소 8글자 최대 20글자로 구성되어야 한다
    @NotBlank(message = "비밀번호는 필수사항 입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 8~20글자, 알파벳, 숫자, 특수문자를 최소 하나씩 입력해야 합니다.")
    private String password;

    //생년월일 형식
    // 년도는 19, 2x로 시작하며 그후에 0 ~ 9 중에 하나로 나올수도 있으며 / 월은 십의 자리가 0일 떄는 일의 자리는 0 ~ 9로 구성되며 /
    // 십의 자리가 1일떄는 일의 자리는 0 ~ 2로 구성되어야 하며 / 일은 십의자리가 0 ~ 2일 떄는 일의 자리는 0 ~ 9로 구성되며 / 십의자리가 3일 떄는 일의 자리는 0~1로 구성되며 /
    // 8자리 생년월일로 입력해야 함
    @NotBlank(message = "생년월일은 필수사항 입니다.")
    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생년월일 8자리 형식에 맞지 않습니다.")
    private String birth;

}
