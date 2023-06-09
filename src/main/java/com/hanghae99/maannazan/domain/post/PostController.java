package com.hanghae99.maannazan.domain.post;

import com.hanghae99.maannazan.domain.file.S3Service;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;


    @Operation(summary = "게시글 업로드(form data 형식)", description = "게시글 작성 + S3이미지 업로드")
    @PostMapping("/posts/{apiId}")
    public ResponseEntity<ResponseMessage<String>> uploadPost(@PathVariable String apiId, PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//        String fileName = postRequestDto.getFile().getOriginalFilename();
        String url = s3Service.uploadFile(postRequestDto.getFile());
        postRequestDto.setS3Url(url);
        postService.createPost(apiId, postRequestDto, userDetails.getUser());
        return  ResponseMessage.SuccessResponse("게시물 작성 성공","");
    }

    @Operation(summary = "게시글 조회 많은 순 3개 조회", description = "게시글 조회수 베스트 3")
    @GetMapping("/posts/best")
    public ResponseEntity<ResponseMessage<List<PostResponseDto>>> getBestPosts() {
    return ResponseMessage.SuccessResponse("게시물 조회 성공",postService.getBestPosts());
    }

    //게시글 전체 조회
    @Operation(summary = "게시글 전체 조회", description = "게시글 리스트 조회")
    @GetMapping("/posts")
    public List<PostResponseDto> getposts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            return postService.getPosts(null);
        } else{
            return postService.getPosts(userDetails.getUser());
        }
    }

    //게시글 좋아요순 조회
    @Operation(summary = "게시글 좋아요 순 전체 조회", description = "게시글 좋아요순 리스트 조회")
    @GetMapping("/posts/likeCount")
    public List<PostResponseDto> getpostsOrderByLikeCnt(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            return postService.getpostsOrderByLikeCnt(null);
        } else{
            return postService.getpostsOrderByLikeCnt(userDetails.getUser());
        }
    }

    //게시글 조회순 조회
    @Operation(summary = "게시글 조회순 전체 조회", description = "게시글 조회순 리스트 조회")
        @GetMapping("/posts/viewCount")
    public List<PostResponseDto> getpostsOrderByViewCount(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            return postService.getpostsOrderByViewCount(null);
        } else{
            return postService.getpostsOrderByViewCount(userDetails.getUser());
        }
    }

    // 게시글 하나 조회
    @Operation(summary = "단일 게시글 상세조회", description = "게시글 상세조회")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseMessage<PostResponseDto>> getPostOne(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails==null){
            return  ResponseMessage.SuccessResponse("단일 게시글 조회 성공", postService.getPostOne(postId, null));
        } else
            return  ResponseMessage.SuccessResponse("단일 게시글 조회 성공", postService.getPostOne(postId, userDetails.getUser()));
        }


    // 게시글 수정
    @Operation(summary = "단일 게시글 업데이트", description = "게시글 업데이트")
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponseMessage<String>> updatePost(@PathVariable Long postId,  PostRequestDto postRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String url = s3Service.uploadFile(postRequestDto.getFile());  //s3에 업로드를 먼저하고 url로 저장하는듯?
        postRequestDto.setS3Url(url);
        return  ResponseMessage.SuccessResponse("게시글 업데이트 성공",postService.updatePost(postId, userDetails.getUser(), postRequestDto));
    }

    // 게시글 삭제
    @Operation(summary = "단일 게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseMessage<String>> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return  ResponseMessage.SuccessResponse("게시글 삭제 완료",postService.deletePost(postId, userDetails.getUser()));

    }
}
