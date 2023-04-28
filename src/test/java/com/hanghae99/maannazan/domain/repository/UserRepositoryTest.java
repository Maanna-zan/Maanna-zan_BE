package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Nested
@DisplayName("users Test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void addUser() {
        //given

        User user = User.builder()
                .userName("장동희")
                .nickName("장동희")
                .phoneNumber("01020948737")
                .email("ehdehdrnt123@naver.com")
                .password("ehd12ehd12!@")
                .birth("19980630")
                .build();

        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isEqualTo(user.getId());
    }

}