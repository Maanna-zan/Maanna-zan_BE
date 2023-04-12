package com.hanghae99.maannazan.domain.mypage;

import com.hanghae99.maannazan.domain.mypage.dto.ChangeNickNameRequestDto;
import com.hanghae99.maannazan.domain.mypage.dto.ChangePasswordRequestDto;
import com.hanghae99.maannazan.domain.mypage.dto.MyPageResponseDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Mypage", description = "마이페이지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class MyPageController {

    private final MyPageService myPageService;
    @Operation(summary = "getMyPage", description = "마이페이지 조회")
    @GetMapping
    public ResponseEntity<ResponseMessage<MyPageResponseDto>> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("성공", myPageService.getMyPage(userDetails.getUser()));
    }
    @Operation(summary = "changeNickName", description = "닉네임 변경")
    @PatchMapping("/change-nickname")
    public ResponseEntity<ResponseMessage<String>> changeNickName(@Validated @RequestBody ChangeNickNameRequestDto changeNickNameRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        myPageService.changeNickName(userDetails.getUser(), changeNickNameRequestDto);
        return ResponseMessage.SuccessResponse("닉네임 변경완료", "");
    }
    @Operation(summary = "changePassword", description = "비밀번호 변경")
    @PatchMapping("/change-password")
    public ResponseEntity<ResponseMessage<String>> changePassword(@Validated @RequestBody ChangePasswordRequestDto changePasswordRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        myPageService.changePassword(userDetails.getUser(), changePasswordRequestDto);
        return ResponseMessage.SuccessResponse("비밀번호 변경완료", "");
    }

}
