package com.hanghae99.maannazan.domain.user;

import com.hanghae99.maannazan.domain.user.dto.*;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "User", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    //회원가입
    @Operation(summary = "signup", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage<String>> signup(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseMessage.SuccessResponse("회원가입 성공","");
    }

    //로그인
    @Operation(summary = "login", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<String>> login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseMessage.SuccessResponse("로그인 성공", "");
    }

    // 유저이메일 중복
    @Operation(summary = "checkEmail", description = "이메일 중복확인")
    @PostMapping("/confirm-email")
    public ResponseEntity<ResponseMessage<String>> checkEmail(@Validated @RequestBody CheckEmailRequestDto checkEmailRequestDto) {
        userService.checkEmail(checkEmailRequestDto);
        return ResponseMessage.SuccessResponse("pass","");
    }

    // 닉네임 중복
    @Operation(summary = "checkNickName", description = "닉네임 중복확인")
    @PostMapping("/confirm-nickName")
    public ResponseEntity<ResponseMessage<String>> checkNickName(@Validated @RequestBody CheckNickNameRequestDto checkNickNameRequestDto) {
        userService.checkNickName(checkNickNameRequestDto);
        return ResponseMessage.SuccessResponse("pass","");
    }

    @PostMapping("/check/findPw")
    @Operation(summary = "checkFindPw", description = "비밀번호 중복확인")
    public ResponseEntity<ResponseMessage<String>> checkFindPw(@Validated @RequestBody CheckFindPwRequestDto checkFindPw) {
        MailDto dto = userService.checkFindPw(checkFindPw);
        userService.mailSend(dto);
        return ResponseMessage.SuccessResponse("이메일로 임시 비밀번호를 보내드렸습니다.","");
    }

    @Operation(summary = "SignOut", description = "회원 탈퇴")
    @DeleteMapping("/signout/{id}")
    public ResponseEntity<ResponseMessage<Object>> signout(@PathVariable Long id, @RequestBody SignoutRequestDto signoutRequestDto) {
        return userService.deleteUser(id, signoutRequestDto);
    }

    @PostMapping("/check/findEmail")
    @Operation(summary = "checkFindEmail", description = "email 찾기")
    public ResponseEntity<ResponseMessage<String>> checkFindEmail(@Validated @RequestBody CheckFindEmailRequestDto checkFindEmailRequestDto) {
        return ResponseMessage.SuccessResponse("email 찾기 성공",userService.checkFindEmail(checkFindEmailRequestDto));
    }
}
