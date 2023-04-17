package com.hanghae99.maannazan.domain.post;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.hanghae99.maannazan.global.exception.CustomErrorCode.ALKOL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3 amazonS3;
    private final KakaoApiRepository kakaoApiRepository;


    @Transactional
    public String createPost(String apiId, PostRequestDto postRequestDto, User user) {
        Kakao kakao = kakaoApiRepository.findByApiId(apiId).orElseThrow(()->new CustomException(ALKOL_NOT_FOUND));
        kakao.postCount(kakao.getNumberOfPosts()+1);
        Post post = new Post(kakao, postRequestDto, user);
        post = postRepository.saveAndFlush(post);
        if (postRequestDto.isBeer() || postRequestDto.isSoju()) {
            categoryRepository.saveAndFlush(new Category(postRequestDto, post));
        }
        return "게시물 작성 성공";
    }

    @Transactional
    public List<PostResponseDto> getPosts(User user){
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : posts) {
            List<Comment> commentList = commentRepository.findByPost(post);
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
            Category category = categoryRepository.findByPostId(post.getId());
            if(user!=null) {
                boolean like = likeRepository.existsByPostIdAndUser(post.getId(), user);
                for (Comment comment : commentList) {
                    commentResponseDtoList.add(new CommentResponseDto(comment));
                }
                if(category!=null){
                    postResponseDtoList.add(new PostResponseDto(category, like, commentResponseDtoList));
                } else {
                    postResponseDtoList.add(new PostResponseDto(post, like ,commentResponseDtoList));
                }
            }
            else {
                if (category != null) {
                    postResponseDtoList.add(new PostResponseDto(category,commentResponseDtoList));
                } else {
                    postResponseDtoList.add(new PostResponseDto(post,commentResponseDtoList));
                }
            }
        }  return postResponseDtoList;
    }

    


    @Transactional
    public PostResponseDto getPostOne(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        post.viewCount(post.getViewCount()+1);
        Category category = categoryRepository.findByPostId(post.getId());
        boolean like = likeRepository.existsByPostIdAndUser(post.getId(), user);
        List<Comment> commentList = commentRepository.findByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        if(category == null){
           return new PostResponseDto(post, like, commentResponseDtoList);
        }
        return new PostResponseDto(category, like, commentResponseDtoList);
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
    public String deletePost(Long postId, User user) throws UnsupportedEncodingException {
        Post post = postRepository.findByUserIdAndId(user.getId(), postId);
//        if(!(user.getId().equals(post.getUser().getId()))){
//            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
//        }
        Category category = categoryRepository.findByPostId(post.getId());
        Likes likes = likeRepository.findByPostIdAndUserId(post.getId(), user.getId());

//        String realName = post.getS3Url().split("/")[3];
//        String decodedObjectPath = URLDecoder.decode(realName, "UTF-8");  //디코딩

        String s3Url = post.getS3Url();
        String encodedFileName = s3Url.substring(s3Url.lastIndexOf("/") + 1); // URL에서 파일 이름 추출
        String decodedFileName = URLDecoder.decode(encodedFileName, "UTF-8"); // 파일 이름 디코딩

        String decodedObjectPath = "sanha--test/" + decodedFileName; // 디코딩된 파일 이름과 버킷 이름을 합쳐서 파일 경로 설정

        amazonS3.deleteObject(new DeleteObjectRequest(bucket, decodedObjectPath));
//        amazonS3.deleteObject(new DeleteObjectRequest(bucket, decodedObjectPath));
//            amazonS3.deleteObject(bucket, realName);   //s3에 올라간 데이터 삭제


        categoryRepository.delete(category);
        postRepository.delete(post);
        likeRepository.delete(likes);
        return "게시글 삭제 완료";

    }

}
