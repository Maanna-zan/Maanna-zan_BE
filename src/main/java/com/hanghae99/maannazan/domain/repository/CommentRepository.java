package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Comment;
import com.hanghae99.maannazan.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
 //   void deleteAllByPost(Post post);
}
