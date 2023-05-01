package com.hanghae99.maannazan.domain.comment;

import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    //    1. 댓글 작성 API
    @Operation(summary = "댓글 작성", description = "댓글 작성")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<ResponseMessage<String>> createComment(@PathVariable Long postId,
                                                                 @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(postId, commentRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("작성 성공", "");
    }

    //    2. 댓글 수정 API
    @Operation(summary = "댓글 업데이트", description = "댓글 업데이트")
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ResponseMessage<String>> updateComment( @PathVariable Long commentId,
                                                                 @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment( commentId, commentRequestDto, userDetails.getUser());
        return ResponseMessage.SuccessResponse("수정 성공", "");
    }
    //    3. 댓글 삭제 API
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseMessage<String>> deleteComment(@PathVariable Long commentId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseMessage.SuccessResponse("삭제 성공", "");
    }

    @Operation(summary = "댓글 리스트 생성", description = "댓글 리스트 생성")
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<ResponseMessage<String>> createCommentList(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.createCommentList(commentRequestDto ,  userDetails.getUser(), commentId);
        return ResponseMessage.SuccessResponse("작성 성공", "");
    }

    @Operation(summary = "대댓글 수정", description = "대댓글 수정")
    @PatchMapping("/recomments/{commentId}")
    public ResponseEntity<ResponseMessage<String>> updateCommentList(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateCommentList(commentId , userDetails.getUser(),commentRequestDto );
        return ResponseMessage.SuccessResponse("수정 성공", "");
    }

    @Operation(summary = "대댓글 삭제", description = "대댓글 삭제")
    @DeleteMapping("/recomments/{commentId}")
    public ResponseEntity<ResponseMessage<String>> deleteCommentList(@PathVariable Long commentId,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.deleteCommentList(commentId, userDetails.getUser());
        return ResponseMessage.SuccessResponse("삭제 성공", "");
    }
}