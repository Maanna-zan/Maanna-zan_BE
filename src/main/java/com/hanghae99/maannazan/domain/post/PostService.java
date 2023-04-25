package com.hanghae99.maannazan.domain.post;


import com.amazonaws.services.s3.AmazonS3;
import com.hanghae99.maannazan.domain.comment.CommentService;
import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;
import com.hanghae99.maannazan.domain.entity.*;
import com.hanghae99.maannazan.domain.like.LikeService;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.KakaoApiRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final LikeService likeService;
    private final CommentService commentService;
    private final AmazonS3 amazonS3;
    private final KakaoApiRepository kakaoApiRepository;


    @Transactional
    public void createPost(String apiId, PostRequestDto postRequestDto, User user) {
        Kakao kakao = kakaoApiRepository.findByApiId(apiId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        kakao.postCount(kakao.getNumberOfPosts() + 1);
        Post post = new Post(kakao, postRequestDto, user);
        postRepository.saveAndFlush(post);
    }

    @Transactional
    public List<PostResponseDto> getBestPosts() {     //게시글 조회수 best3
        List<Post> posts = getPostListOrderByViewCount();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(int i=0; i<3; i++){
            postResponseDtoList.add(new PostResponseDto(posts.get(i)));
        }
        return postResponseDtoList;
    }


    @Transactional
    public List<PostResponseDto> getPosts(User user) {  // 게시글 전체조회
        List<Post> posts = getPostList();
        return getPostResponseDtoList(user, posts);
    }




    public List<PostResponseDto> getpostsOrderByLikeCnt(User user) { // 게시글 좋아요 순 조회
        List<Post> posts = getPostListOrderByLikecntDesc();
        return getPostResponseDtoList(user, posts);
    }


    public List<PostResponseDto> getpostsOrderByViewCount(User user) { // 게시글 조회순 조회
        List<Post> postList = getPostListOrderByViewCount();
        return getPostResponseDtoList(user, postList);
    }


    @Transactional
    public PostResponseDto getPostOne(Long postId, User user) { //단일 게시글 조회
        Post post = getPostByPostId(postId);
        post.viewCount(post.getViewCount() + 1);
        boolean like = likeService.getPostLike(post, user);
        List<Comment> commentList = commentService.getCommentListByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment, comment.getId()));
        }
        return new PostResponseDto(post, like, commentResponseDtoList);
    }



    @Transactional
    public String updatePost(Long postId, User user, PostRequestDto requestDto) { //게시글 업데이트
        Post post = getPostByUserIdAndPostId(user, postId);
        if(!(user.getId().equals(post.getUser().getId()))){
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        post.update(requestDto, user);
        return "업데이트 성공";
    }



    @Transactional
    public String deletePost(Long postId, User user) { //게시글 삭제
        Post post = getPostByUserIdAndPostId(user, postId);
        if (!(user.getId().equals(post.getUser().getId()))) {
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        Likes likes = likeService.getPostLikes(post, user);

        try {
            String realName = post.getS3Url().split("/")[3];
            boolean isObjectExist = amazonS3.doesObjectExist(bucket, realName);
            if (isObjectExist) {
                amazonS3.deleteObject(bucket, realName);   //s3에 올라간 데이터 삭제
            } else {
                throw new CustomException(CustomErrorCode.S3_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (likes != null) {
            likeService.deleteLikes(likes);
        }
        postRepository.delete(post);
        return "게시글 삭제 완료";
    }


    //메서드
    public List<Post> getPostListOrderByViewCount(){    // 게시글 조회순으로 가져오기
        return postRepository.findAllByOrderByViewCountDesc();
    }


    public List<Post> getPostByKakaoApiId(Kakao kakao){    // 술집에 저장된 게시글 확인
        return postRepository.findByKakaoApiId(kakao.getApiId());
    }

    public Post getPostByUserIdAndPostId(User user, Long postId){    // 내가 작성한 게시글 확인
        return postRepository.findByUserIdAndId(user.getId(), postId);
    }

    public List<Post> getPostList(){    // 게시글 전체 조회
        return postRepository.findAll();
    }

    public List<Post> getPostListOrderByLikecntDesc(){    // 게시글 좋아요순 조회
        return postRepository.findAllByOrderByLikecntDesc();
    }

    public Post getPostByPostId(Long postId){    // 단일 게시글 조회 (상세조회)
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
    }

    public Page<Post> getPostOrderByCreatedAtDesc(User user, Pageable pageable){    // 게시글 최신순 조회 (페이지네이션)
        return postRepository.findByUserOrderByCreatedAtDesc(user,pageable);
    }

    public Page<Post> getPostList(Pageable pageable){    // 게시글 최신순 조회 (페이지네이션)
        return postRepository.findAll(pageable);
    }

    public List<Post> getPostByUserId(Long id){    // 게시글 최신순 조회 (페이지네이션)
        return postRepository.findByUserId(id);
    }

    public void deletePostAll(List<Post> postList){    // 게시글 전체 삭제 (회원탈퇴)
        postRepository.deleteAll(postList);
    }
    private List<PostResponseDto> getPostResponseDtoList(User user, List<Post> posts) {   //게시글 리스트 조회
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Post post : posts) {
            List<Comment> commentList = commentService.getCommentListByPost(post);
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
            if (user != null) {
                boolean like = likeService.getPostLike(post, user);
                for (Comment comment : commentList) {
                    commentResponseDtoList.add(new CommentResponseDto(comment, comment.getId()));
                }
                postResponseDtoList.add(new PostResponseDto(post, like, commentResponseDtoList));
            } else {
                postResponseDtoList.add(new PostResponseDto(post, commentResponseDtoList));
            }
        }
        return postResponseDtoList;
    }
}
