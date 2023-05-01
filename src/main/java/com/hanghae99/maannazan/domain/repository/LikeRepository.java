package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByUserAndPost(User user, Post post);


    boolean existsByPostIdAndUser(Long postId, User user);

    Likes findByPostIdAndUserId(Long PostId, Long UserId);


    List<Likes> findByUserId(Long id);

    List<Likes> findAllByStatusAndUser(boolean Status,User user);

}
