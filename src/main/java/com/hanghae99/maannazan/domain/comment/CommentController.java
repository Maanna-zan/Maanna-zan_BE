package com.hanghae99.maannazan.domain.comment;

import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    //    1. 댓글 작성 API
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ResponseMessage<String>> createComment(@PathVariable Long postId,
                                                                 @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(postId, commentRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("가입 성공", "");
    }

    //    2. 댓글 수정 API
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseMessage<String>> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                                 @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(postId, commentId, commentRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("수정 성공", "");
    }
    //    3. 댓글 삭제 API
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseMessage<String>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(postId, commentId, userDetails.getUser());
        return ResponseMessage.SuccessResponse("삭제 성공", "");
    }

}