package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Kakao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface KakaoApiRepository extends JpaRepository<Kakao, Long> {

    List<Kakao> findAllByApiId(String apiId);

    Optional<Kakao> findByApiId(String apiId);
}
