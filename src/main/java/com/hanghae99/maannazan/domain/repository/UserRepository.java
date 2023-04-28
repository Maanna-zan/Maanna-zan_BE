package com.hanghae99.maannazan.domain.repository;


import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByNickName(String nickName);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByUserNameAndPhoneNumber(String userName, String phoneNumber);

    User findByUserName(String userName);
}
