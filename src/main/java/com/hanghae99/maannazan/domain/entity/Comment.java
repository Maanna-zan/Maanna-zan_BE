package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @JoinColumn(name = "Post_ID")
    @ManyToOne
    private Post post;

    public Comment(Post post, User user, CommentRequestDto commentRequestDto) {
        this.post = post;
        this.user = user;
        this.content = commentRequestDto.getContent();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}