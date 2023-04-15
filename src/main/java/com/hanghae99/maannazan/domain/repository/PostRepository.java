package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByUserIdAndId(Long id, Long postId);

    List<Post> findByUserId(Long id);

    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);


    List<Post> findByKakaoApiId(String apiId);
}
