package com.hanghae99.maannazan.domain.DisLike;

import com.hanghae99.maannazan.domain.entity.DisLike;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class DisLikeController {
    private final DisLikeService disLikeService;

    @PutMapping("/posts/disLike/{postId}")
    public ResponseEntity<ResponseMessage<String>> disLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("클릭 성공",disLikeService.disLike(postId, userDetails.getUser()));
    }

}
