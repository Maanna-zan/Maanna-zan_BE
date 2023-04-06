package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Likes;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByUserAndPost(User user, Post post);

    boolean existsByPostIdAndUser(Long postId, User user);

    Likes findByPostIdAndUserId(Long PostId, Long UserId);
}
