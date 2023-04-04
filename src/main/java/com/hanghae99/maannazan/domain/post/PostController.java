package com.hanghae99.maannazan.domain.post;

import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @PostMapping("/posts")
    public ResponseEntity<ResponseMessage<String>> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  ResponseMessage.SuccessResponse("게시물 작성 성공",postService.createPost(postRequestDto, userDetails.getUser()));
    }

    // 게시글 하나 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseMessage<PostResponseDto>> getPostOne(@PathVariable Long postId){
        return  ResponseMessage.SuccessResponse("단일 게시글 조회 성공", postService.getPostOne(postId));
    }

//    // 범위 내 게시글 전체 조회
//    @GetMapping("/posts/{postId}")
//    public ResponseEntity getPosts(@PathVariable Long postId, @RequestParam(required = false) PostRequestDto postRequestDto){
//        return  ResponseMessage.SuccessResponse("범위 내 게시글 조회 성공",postService.getPosts(postId));
//    }

    // 게시글 수정
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponseMessage<String>> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return  ResponseMessage.SuccessResponse("게시글 업데이트 성공",postService.updatePost(postId, userDetails.getUser(), requestDto));
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseMessage<String>> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  ResponseMessage.SuccessResponse("게시글 삭제 완료",postService.deletePost(postId, userDetails.getUser()));

    }

}
