package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByUserIdAndId(Long id, Long postId);

    List<Post> findByUserId(Long id);

    List<Post> findByUserOrderByCreatedAtDesc(User user);



    List<Post> findByApiId(String apiId);
}
