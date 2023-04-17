package com.hanghae99.maannazan.domain.like;


import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    @Operation(summary = "like", description = "게시글 좋아요 토글")
    @PutMapping("/posts/like/{postId}")
    public ResponseEntity<ResponseMessage<String>> like(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("클릭 성공",likeService.like(postId, userDetails.getUser()));
    }
    @Operation(summary = "commentLike", description = "댓글 좋아요 토글")
    @PutMapping("/comments/like/{commentId}")
    public ResponseEntity<ResponseMessage<String>> commentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("클릭 성공",likeService.commentLike(commentId, userDetails.getUser()));
    }

    @Operation(summary = "kakaoApiLike", description = "술집 좋아요 토글")
    @PutMapping("/bar/like/{apiId}")
    public ResponseEntity<ResponseMessage<String>> roomLike(@PathVariable String apiId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("클릭 성공",likeService.roomLike(apiId, userDetails.getUser()));
    }
}
