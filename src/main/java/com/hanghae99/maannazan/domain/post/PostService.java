package com.hanghae99.maannazan.domain.post;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;
import com.hanghae99.maannazan.domain.entity.*;

import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.*;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;
    private final DisLikeRepository disLikeRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3 amazonS3;


    @Transactional
    public String createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto, user);
        post = postRepository.saveAndFlush(post);
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
            if(user!=null) {
                boolean like = likeRepository.existsByPostIdAndUser(post.getId(), user);
                boolean disLike = disLikeRepository.existsByPostIdAndUser(post.getId(), user);
                List<Comment> commentList = commentRepository.findByPost(post);
                List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
                for (Comment comment : commentList) {
                    commentResponseDtoList.add(new CommentResponseDto(comment));
                }
                Category category = categoryRepository.findByPostId(post.getId());
                if(category!=null){
                    postResponseDtoList.add(new PostResponseDto(category, like, disLike, commentResponseDtoList));
                } else {
                    postResponseDtoList.add(new PostResponseDto(post, like, disLike ,commentResponseDtoList));
                }
            }
        } return postResponseDtoList;
    }


    @Transactional(readOnly = true)
    public PostResponseDto getPostOne(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        Category category = categoryRepository.findByPostId(post.getId());
        boolean like = likeRepository.existsByPostIdAndUser(post.getId(), user);
        boolean disLike = disLikeRepository.existsByPostIdAndUser(post.getId(), user);
        List<Comment> commentList = commentRepository.findByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        if(category == null){
           return new PostResponseDto(post, like, disLike, commentResponseDtoList);
        }
        return new PostResponseDto(category, like, disLike, commentResponseDtoList);
    }



    @Transactional
    public String updatePost(Long postId, User user, PostRequestDto requestDto) {

        Post post = postRepository.findByUserIdAndId(user.getId(), postId);
        if(!(user.getId().equals(post.getUser().getId()))){
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        post.update(requestDto, user);
        return "업데이트 성공";
    }

    @Transactional
    public String deletePost(Long postId, User user) {
        Post post = postRepository.findByUserIdAndId(user.getId(), postId);
        if(!(user.getId().equals(post.getUser().getId()))){
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        Category category = categoryRepository.findByPostId(post.getId());
        Likes likes = likeRepository.findByPostIdAndUserId(post.getId(), user.getId());

//        amazonS3.deleteObject(bucket, post.getFileName());   //s3에 올라간 데이터 삭제

        categoryRepository.delete(category);
        postRepository.delete(post);
        likeRepository.delete(likes);
        return "게시글 삭제 완료";

    }
}
