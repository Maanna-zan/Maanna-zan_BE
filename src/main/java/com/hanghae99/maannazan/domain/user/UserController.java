package com.hanghae99.maannazan.domain.user;

import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.UserRepository;
import com.hanghae99.maannazan.domain.user.dto.*;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Tag(name = "User", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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


    @Transactional
    @DeleteMapping("/signout/{id}")
    public ResponseEntity<ResponseMessage<Object>> signout(@PathVariable Long id, @RequestBody SignoutRequestDto signoutRequestDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseMessage.ErrorResponse(CustomErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(signoutRequestDto.getPassword(), user.get().getPassword())) {
            return ResponseMessage.ErrorResponse(CustomErrorCode.INVALID_PASSWORD);
        }
        userRepository.deleteById(id);
        return ResponseMessage.SuccessResponse("회원탈퇴 성공", "");
    }
}
