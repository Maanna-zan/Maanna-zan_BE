package com.hanghae99.maannazan.domain.like;


import com.hanghae99.maannazan.domain.entity.Comment;
import com.hanghae99.maannazan.domain.entity.Likes;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.CommentRepository;
import com.hanghae99.maannazan.domain.repository.LikeRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {


    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

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

//    @Transactional
//    public String commentLike(Long commentId, User user) {
//        // 해당 사용자 정보와 게시글 정보를 가져온다.
//        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
//        Likes likes = likeRepository.findByUserAndComment(user, comment);
//
//        if (likes != null) { // 이미 좋아요를 눌렀다면 좋아요 취소
//            likeRepository.delete(likes);
//            comment.likeCount(comment.getLikecnt() - 1);
//            commentRepository.save(comment);
//            return "좋아요 취소";
//        } else { // 좋아요를 누르지 않았다면 좋아요 추가
//            likes = new Likes(comment, user);
//            likeRepository.save(likes);
//            comment.likeCount(comment.getLikecnt() + 1);
//            commentRepository.save(comment);
//            return "좋아요 성공";
//        }
//    }

}




