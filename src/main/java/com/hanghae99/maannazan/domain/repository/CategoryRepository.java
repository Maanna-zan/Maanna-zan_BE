package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Category;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByPostId(Long postId);



}
