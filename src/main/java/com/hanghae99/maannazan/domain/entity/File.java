package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileTitle;

    @Column
    private String s3Url;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    Post post;


    public File(String fileTitle, String s3Url) {
        this.fileTitle = fileTitle;
        this.s3Url = s3Url;
    }

    public File(Post post, PostRequestDto postRequestDto, User user) {
        this.fileTitle = postRequestDto.getFileTitle();
        this.s3Url = postRequestDto.getS3Url();
        this.post = post;
        this.user = user;
    }

    @Override
    public String toString() {     //문자 형식으로 줘야함
        return "FileEntity{" +
                "id=" + id +
                ", fileTitle='" + fileTitle + '\'' +
                ", s3Url='" + s3Url + '\'' +
                '}';
    }
}