package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search ,Long> {
    List<Search> findByUserId(Long id);
}
