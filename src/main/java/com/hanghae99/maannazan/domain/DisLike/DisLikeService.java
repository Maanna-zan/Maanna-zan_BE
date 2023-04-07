package com.hanghae99.maannazan.domain.DisLike;

import com.hanghae99.maannazan.domain.entity.DisLike;
import com.hanghae99.maannazan.domain.entity.Likes;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.DisLikeRepository;
import com.hanghae99.maannazan.domain.repository.LikeRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class DisLikeService {

    private final PostRepository postRepository;

    private final DisLikeRepository dislikeRepository;


    @Transactional
    public String disLike(Long postId, User user) {
        // 해당 사용자 정보와 게시글 정보를 가져온다.
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND));
        DisLike disLike = dislikeRepository.findByUserAndPost(user, post);

        if (disLike != null) { // 이미 좋아요를 눌렀다면 좋아요 취소
            dislikeRepository.delete(disLike);
            post.DisLikeCount(post.getDisLikecnt() - 1);
            postRepository.save(post);
            return "싫어요 취소";
        } else { // 좋아요를 누르지 않았다면 좋아요 추가
            disLike = new DisLike(post, user);
            dislikeRepository.save(disLike);
            post.DisLikeCount(post.getDisLikecnt() + 1);
            postRepository.save(post);
            return "싫어요 성공";
        }
    }
}
