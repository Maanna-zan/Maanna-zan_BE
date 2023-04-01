package com.hanghae99.maannazan.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.hanghae99.maannazan.global.exception.CustomErrorCode.*;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ResponseMessage<Object>> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ResponseMessage.ErrorResponse(e.getErrorCode());

    }


    //    @Valid 오류 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage<String>> handleNotValidException(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(Objects.requireNonNull(e.getFieldError()).getDefaultMessage()).split(":")[0];
        return ResponseMessage.ValidResponse(message);
    }
}
