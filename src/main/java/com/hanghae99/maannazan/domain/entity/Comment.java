package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private int likecnt;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public Comment(Post post, User user, CommentRequestDto commentRequestDto) {
        this.post = post;
        this.user = user;
        this.content = commentRequestDto.getContent();
        this.likecnt = 0;
    }

    public Comment(CommentRequestDto commentRequestDto, User user, Comment parentComment) {
        this.content = commentRequestDto.getContent();
        this.user = user;
        this.parent = parentComment;
        this.likecnt = 0;

    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    public void likeCount(int plusOrMinus) {  //좋아요 개수
        this.likecnt = plusOrMinus;
    }

    public List<Comment> getChildComments() {
        return this.children;
    }


}