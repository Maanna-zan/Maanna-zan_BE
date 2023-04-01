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
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    //    1. 댓글 작성 API
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ResponseMessage> createComment(@PathVariable Long postId,
                                                         @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("조회 성공", commentService.createComment(postId, commentRequestDto, userDetails.getUser()));    }

    //    2. 댓글 수정 API
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseMessage> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                            @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("조회 성공", commentService.updateComment(postId, commentId, commentRequestDto, userDetails.getUser()));
    }
    //    3. 댓글 삭제 API
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ResponseMessage> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("조회 성공", commentService.deleteComment(postId, commentId, userDetails.getUser()));
    }

}