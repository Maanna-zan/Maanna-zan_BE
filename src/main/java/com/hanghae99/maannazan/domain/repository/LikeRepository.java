package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}
