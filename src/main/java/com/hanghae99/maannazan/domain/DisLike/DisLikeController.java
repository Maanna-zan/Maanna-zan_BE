package com.hanghae99.maannazan.domain.DisLike;

import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "Dislike", description = "싫어요 관련 API")
@RestController
@RequiredArgsConstructor

public class DisLikeController {
    private final DisLikeService disLikeService;
    @Operation(summary = "disLike", description = "싫어요 토글")
    @PutMapping("/posts/disLike/{postId}")
    public ResponseEntity<ResponseMessage<String>> disLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("클릭 성공",disLikeService.disLike(postId, userDetails.getUser()));
    }

}
