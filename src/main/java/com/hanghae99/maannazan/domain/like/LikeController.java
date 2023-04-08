package com.hanghae99.maannazan.domain.like;


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
public class LikeController {

    private final LikeService likeService;

    @PutMapping("/posts/like/{postId}")
    public ResponseEntity<ResponseMessage<String>> like(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("클릭 성공",likeService.like(postId, userDetails.getUser()));
    }

    @PutMapping("/comments/like/{commentId}")
    public ResponseEntity<ResponseMessage<String>> commentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("클릭 성공",likeService.commentLike(commentId, userDetails.getUser()));
    }
}
