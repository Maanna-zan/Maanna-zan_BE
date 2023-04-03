package com.hanghae99.maannazan.domain.entity;

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
}