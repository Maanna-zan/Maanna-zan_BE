package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.user.dto.CheckFindPwRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String userName;

    @Column(unique = true)
    private String nickName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    private String password;


    private String birth;

    public User(String userName, String nickName, String email, String phoneNumber, String password, String birth) {
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.birth = birth;
    }

    public void update(String str, String email) {
        this.password = str;
        this.email = email;
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}