package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.DisLike;
import com.hanghae99.maannazan.domain.entity.Likes;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisLikeRepository extends JpaRepository<DisLike,Long> {
    DisLike findByUserAndPost(User user, Post post);

    boolean existsByPostIdAndUser(Long postId, User user);

}
