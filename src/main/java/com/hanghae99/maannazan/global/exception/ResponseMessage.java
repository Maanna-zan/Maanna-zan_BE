package com.hanghae99.maannazan.global.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


//record 클래스
//해당 record 클래스는 final 클래스이라 상속할 수 없다.
// 각 필드는 private final 필드로 정의된다.
// 모든 필드를 초기화하는 RequiredAllArgument 생성자가 생성된다.
@Builder
public record ResponseMessage<T>(String message, int statusCode, T data) {
    public static ResponseEntity<ResponseMessage<Object>> ErrorResponse(CustomErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseMessage.<Object>builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .data("")
                        .build()
                );
    }

    public static <T> ResponseEntity<ResponseMessage<T>> SuccessResponse(String message, T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseMessage.<T>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(message)
                        .data(data)
                        .build()
                );
    }

    //valid 오류처리
    public static <T> ResponseEntity<ResponseMessage<T>>ValidResponse(String message){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseMessage.<T>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(message)
                        .build()
                );

    }
}

