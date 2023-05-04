package com.hanghae99.maannazan.domain.user;

import com.hanghae99.maannazan.domain.user.dto.*;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "User", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    //회원가입
    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage<String>> signup(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseMessage.SuccessResponse("회원가입 성공","");
    }

    //로그인
    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<String>> login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        if (loginRequestDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$")){
            return ResponseMessage.SuccessResponse("로그인 성공", userService.login(loginRequestDto, response));
        }else
            return ResponseMessage.SuccessResponse("비밀번호 변경이 필요합니다", userService.login(loginRequestDto, response));
    }


    // 닉네임 중복
    @Operation(summary = "닉네임 중복확인", description = "닉네임 중복확인")
    @PostMapping("/confirm-nickName")
    public ResponseEntity<ResponseMessage<String>> checkNickName(@Validated @RequestBody CheckNickNameRequestDto checkNickNameRequestDto) {
        userService.checkNickName(checkNickNameRequestDto);
        return ResponseMessage.SuccessResponse("pass","");
    }

    @Operation(summary = "email 중복확인", description = "닉네임 중복확인")
    @PostMapping("/confirm-email")
    public ResponseEntity<ResponseMessage<String>> checkEmail(@Validated @RequestBody CheckEmailRequestDto checkEmailRequestDto) {
        userService.checkEmail(checkEmailRequestDto);
        return ResponseMessage.SuccessResponse("pass","");
    }

    @PostMapping("/check/findPw")
    @Operation(summary = "비밀번호 중복확인", description = "비밀번호 중복확인")
    public ResponseEntity<ResponseMessage<String>> checkFindPw(@Validated @RequestBody CheckFindPwRequestDto checkFindPw) {
        MailDto dto = userService.checkFindPw(checkFindPw);
        userService.mailSend(dto);
        return ResponseMessage.SuccessResponse("이메일로 임시 비밀번호를 보내드렸습니다.","");
    }

    @Operation(summary = "이메일 중복확인, 인증번호 이메일 발송", description = "이메일 중복확인, 인증번호 이메일 발송")
    @PostMapping("/check/email")
    public ResponseEntity<ResponseMessage<String>> emailNumber(@Validated @RequestBody CheckEmailRequestDto checkEmailRequestDto) {
        MailDto dto = userService.emailNumber(checkEmailRequestDto);
        userService.mailSend(dto);
        return ResponseMessage.SuccessResponse("이메일로 인증번호를 보내드렸습니다.","");
    }

    @Operation(summary = "", description = "이메일 중복확인")
    @PostMapping("/check/number")
    public ResponseEntity<ResponseMessage<String>> checkNumber(@RequestBody CheckNumberRequestDto checkNumberRequestDto) {
        userService.numberCheck(checkNumberRequestDto);
        return ResponseMessage.SuccessResponse("인증이 완료되었습니다","");
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    @PutMapping("/signout/{id}")
    public ResponseEntity<ResponseMessage<Object>> signout(@PathVariable Long id,@RequestBody SignoutRequestDto signoutRequestDto) {
        return userService.deleteUser(id, signoutRequestDto);
    }

    @PostMapping("/check/findEmail")
    @Operation(summary = "email 찾기", description = "email 찾기")
    public ResponseEntity<ResponseMessage<String>> checkFindEmail(@Validated @RequestBody CheckFindEmailRequestDto checkFindEmailRequestDto) {
        return ResponseMessage.SuccessResponse("email 찾기 성공",userService.checkFindEmail(checkFindEmailRequestDto));
    }
}
