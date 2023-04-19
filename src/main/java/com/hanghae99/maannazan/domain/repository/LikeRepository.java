package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByUserAndPost(User user, Post post);
    Likes findByUserAndKakao(User user, Kakao kakao);

    Likes findByUserAndComment(User user, Comment comment);

    boolean existsByPostIdAndUser(Long postId, User user);

    Likes findByPostIdAndUserId(Long PostId, Long UserId);

    boolean existsByKakaoApiIdAndUser(String ApiId, User user);

    List<Likes> findByUserId(Long id);
}
