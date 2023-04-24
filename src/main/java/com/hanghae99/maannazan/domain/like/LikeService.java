package com.hanghae99.maannazan.domain.like;


import com.hanghae99.maannazan.domain.entity.*;
import com.hanghae99.maannazan.domain.repository.CommentRepository;
import com.hanghae99.maannazan.domain.repository.KakaoApiRepository;
import com.hanghae99.maannazan.domain.repository.LikeRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {


    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final KakaoApiRepository kakaoApiRepository;

    @Transactional
    public String like(Long postId, User user) {
        // 해당 사용자 정보와 게시글 정보를 가져온다.
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        Likes likes = likeRepository.findByUserAndPost(user, post);

        if (likes != null) { // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.delete(likes);
            post.likeCount(post.getLikecnt() - 1);
            postRepository.save(post);
            return "좋아요 취소";
        } else { // 좋아요를 누르지 않았다면 좋아요 추가
            likes = new Likes(post, user);
            likeRepository.save(likes);
            post.likeCount(post.getLikecnt() + 1);
            postRepository.save(post);
            return "좋아요 성공";
        }
    }

    @Transactional
    public String roomLike(String apiId, User user) {
        // 해당 사용자 정보와 게시글 정보를 가져온다.
        Kakao kakao = kakaoApiRepository.findByApiId(apiId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        Likes likes = likeRepository.findByUserAndKakao(user, kakao);
        if (likes != null) { // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.delete(likes);
            kakao.likeCount(kakao.getRoomLikecnt() - 1);
            kakaoApiRepository.save(kakao);
            return "좋아요 취소";
        } else { // 좋아요를 누르지 않았다면 좋아요 추가
            likes = new Likes(kakao, user);
            likeRepository.save(likes);
            kakao.likeCount(kakao.getRoomLikecnt() + 1);
            kakaoApiRepository.save(kakao);
            return "좋아요 성공";
        }
    }



    @Transactional
    public String commentLike(Long commentId, User user) {
        // 해당 사용자 정보와 게시글 정보를 가져온다.
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        Likes likes = likeRepository.findByUserAndComment(user, comment);

        if (likes != null) { // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.delete(likes);
            comment.likeCount(comment.getLikecnt() - 1);
            commentRepository.save(comment);
            return "좋아요 취소";
        } else { // 좋아요를 누르지 않았다면 좋아요 추가
            likes = new Likes(comment, user);
            likeRepository.save(likes);
            comment.likeCount(comment.getLikecnt() + 1);
            commentRepository.save(comment);
            return "좋아요 성공";
        }
    }

    
    //메서드
    public boolean getPostLike(Post post, User user){    // 게시글 좋아요 상태 확인 (true면 좋아요 누른 상태)
        return likeRepository.existsByPostIdAndUser(post.getId(),user);
    }

    public boolean getAlkolLike(String apiId, User user){    // 게시글 좋아요 상태 확인 (true면 좋아요 누른 상태)
        return likeRepository.existsByKakaoApiIdAndUser(apiId, user);
    }

    public boolean getCommentLike(Post post, User user){    // 게시글 좋아요 상태 확인 (true면 좋아요 누른 상태)
        return likeRepository.existsByPostIdAndUser(post.getId(),user);
    }
    public List<Likes> getUserLike(Long id){    // 게시글 좋아요 상태 확인 (true면 좋아요 누른 상태)
        return likeRepository.findByUserId(id);
    }

    public Likes getPostLikes(Post post, User user){    // 게시글 좋아요 삭제를 위한 조회
        return likeRepository.findByPostIdAndUserId(post.getId(), user.getId());
    }

    public void deleteLikes(Likes likes){    // 게시글에 달린 좋아요 삭제
        likeRepository.delete(likes);
    }

    public void deleteLikesAll(List<Likes> likes){    // 게시글에 달린 좋아요 전체 삭제(회원탈퇴)
        likeRepository.deleteAll(likes);
    }
}




