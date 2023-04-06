package com.hanghae99.maannazan.domain.post;

import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;
import com.hanghae99.maannazan.domain.entity.*;
import com.hanghae99.maannazan.domain.file.FileRepository;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.*;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final FileRepository fileRepository;
    private final LikeRepository likeRepository;


    public String createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto, user);
        post = postRepository.saveAndFlush(post);
        File file = new File(post, postRequestDto, user);   //
        fileRepository.save(file);                          //
        if (postRequestDto.isBeer() || postRequestDto.isSoju()) {
            categoryRepository.saveAndFlush(new Category(postRequestDto, post));
        }
        return "게시물 작성 성공";
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts(User user){
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : posts) {
            boolean like = likeRepository.existsByPostIdAndUser(post.getId(), user);
            postResponseDtoList.add(new PostResponseDto(post, like));
        } return postResponseDtoList;
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostOne(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        File file = fileRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        Category category = categoryRepository.findByPostId(post.getId());
        if(category == null){
           return new PostResponseDto(post, file);
        }
        return new PostResponseDto(category, file);

    }



//    @Transactional(readOnly = true)     //개빡세다 전체 게시글 조회가 아니라 반경 x키로 안에 있는 게시글들을 조회해야해서..... 그리고 카테고리 별 검색 기능까지...
//    public List<PostResponseDto> getPosts(double x, double y, String category) {
//        List<Post> postList = postRepository.findByXAndY(x, y);    //findByXAndY가 아니라 x,y로 지정한 반경 몇키로 내의 게시글들을 조회해야함
//        List<Category> categoryList = categoryRepository.findAll();
//        List<PostResponseDto> PostResponseDtoList = new ArrayList<>();
//        for (Category category : categoryList) {
//            PostResponseDtoList.add(new PostResponseDto(category));
//
//        }
//        return PostResponseDtoList;
//    }

    @Transactional
    public String updatePost(Long postId, User user, PostRequestDto requestDto) {
        Post post = postRepository.findByUserIdAndId(user.getId(), postId);
        if(!(user.getId().equals(post.getUser().getId()))){
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        post.update(requestDto);
        return "업데이트 성공";
    }

    @Transactional
    public String deletePost(Long postId, User user) {
        Post post = postRepository.findByUserIdAndId(user.getId(), postId);
        if(!(user.getId().equals(post.getUser().getId()))){
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        Category category = categoryRepository.findByPostId(post.getId());
        categoryRepository.delete(category);
        postRepository.delete(post);
        return "게시글 삭제 완료";
    }
}
