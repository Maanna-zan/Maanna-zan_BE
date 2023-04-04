package com.hanghae99.maannazan.domain.mypage;

import com.hanghae99.maannazan.domain.mypage.dto.MyPageResponseDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ResponseMessage<MyPageResponseDto>> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("성공", myPageService.getMyPage(userDetails.getUser()));
    }


}
