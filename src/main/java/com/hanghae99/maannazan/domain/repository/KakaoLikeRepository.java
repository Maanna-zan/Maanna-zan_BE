package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Kakao;
import com.hanghae99.maannazan.domain.entity.KakaoLikes;
import com.hanghae99.maannazan.domain.entity.Likes;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KakaoLikeRepository extends JpaRepository<KakaoLikes, Long> {

    KakaoLikes findByUserAndKakao(User user, Kakao kakao);

    List<KakaoLikes> findAllByStatusAndUser(boolean Status, User user);

    boolean existsByKakaoApiIdAndUser(String ApiId, User user);

    KakaoLikes findByKakaoApiIdAndUser(String apiId, User user);
}
