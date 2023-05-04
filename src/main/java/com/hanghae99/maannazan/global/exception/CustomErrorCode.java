package com.hanghae99.maannazan.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    INVALID_PASSWORD(BAD_REQUEST,"비밀번호가 유효하지 않습니다"),
    DUPLICATE_EMAIL(BAD_REQUEST, "중복된 이메일이 존재합니다"),
    DUPLICATE_NICKNAME(BAD_REQUEST, "중복된 닉네임이 존재합니다"),
    DUPLICATE_PHONENUMBER(BAD_REQUEST, "중복된 번호가 존재합니다"),
    DUPLICATE_PASSWORD(BAD_REQUEST, "현재 비밀번호로 변경이 불가능합니다"),
    NOT_PROPER_EMAIL_OR_PASSWORD(BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다."),
    NOT_PROPER_INPUTFORM(BAD_REQUEST, "입력한 형식이 맞지 않습니다."),
    NOT_PROPER_URLFORM(BAD_REQUEST, "입력한 URL 형식이 맞지 않습니다."),
    NOT_AUTHOR(BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    TITLE_IS_EMPTY(BAD_REQUEST, "제목에 빈 값이 올 수 없습니다"),   //캘린더, 게시글 테이블
    CONTENT_IS_EMPTY(BAD_REQUEST, "내용에 빈 값이 올 수 없습니다"), //캘린더, 게시글, 댓글 테이블
    DATE_IS_EMPTY(BAD_REQUEST, "날짜를 선택해주세요"),
    FALSE_ID(BAD_REQUEST, "잘못된 ID입니다"),
    FALSE_API_ID(BAD_REQUEST, "잘못된 술집 ID입니다"), //게시글 테이블
    TASTE_VALUE_IS_FALSE(BAD_REQUEST, "평점(맛) 형식이 잘못되었습니다"), //게시글 테이블
    SERVICE_VALUE_IS_FALSE(BAD_REQUEST, "평점(서비스) 형식이 잘못되었습니다"), //게시글 테이블
    ATMOSPHERE_VALUE_IS_FALSE(BAD_REQUEST, "평점(분위기) 형식이 잘못되었습니다"), //게시글 테이블
    SATISFACTION_VALUE_IS_FALSE(BAD_REQUEST, "평점(만족도) 형식이 잘못되었습니다"), //게시글 테이블
    SEARCH_WORD_IS_EMPTY(BAD_REQUEST, "평점(만족도) 형식이 잘못되었습니다"), //게시글 테이블
    X1_CAN_NOT_BE_NULL(BAD_REQUEST, "두명 이상 검색해주세요"), // 위치검색
    DATE_IS_NULL(BAD_REQUEST, "날짜를 선택해주세요"), // 위치검색





    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    POST_NOT_FOUND(NOT_FOUND, "선택한 게시물을 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(NOT_FOUND, "선택한 일정을 찾을 수 없습니다."),
    S3_NOT_FOUND(NOT_FOUND, "선택한 이미지를 찾을 수 없습니다."),
    ALKOL_NOT_FOUND(NOT_FOUND, "선택한 술집을 찾을 수 없습니다."),
    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_AND_PHONENUMBER_NOT_FOUND(NOT_FOUND, "등록된 정보가 없습니다"),
    COMMENT_NOT_FOUND(NOT_FOUND, "선택한 댓글을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(NOT_FOUND,"등록되지 않은 이메일입니다."),
    CALENDAR_NOT_FOUND(NOT_FOUND,"선택한 캘린더를 찾을 수 없습니다"),
    CALENDAR_DTO_NOT_FOUND(NOT_FOUND,"저장할 정보가 없습니다."),
    CERTIFICATIONNUMBER_NOT_FOUND(NOT_FOUND,"인증번호가 일치하지않습니다");



    private final HttpStatus httpStatus;
    private final String message;
}
