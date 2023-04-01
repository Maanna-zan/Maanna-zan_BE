package com.hanghae99.maannazan.domain.post;

import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(postRequestDto, userDetails.getUser());
    }

    // 게시글 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPostList(){
        return postService.findAllPost();
    }

    // 게시글 수정
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.updatePost(postId, postRequestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    // ResponseEntity -> HTTP 응답 코드를 설정할 수 있다(성공(200), 생성(201),
    // 요청이 잘못됨(400), 권한 없음(403), 찾을 수 없음(404), 서버 오류(500) 코드를 사용!!!
    public ResponseEntity<?> delete(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.deletePost(postId,postRequestDto,userDetails.getUser());
    }

}
