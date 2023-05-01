package com.hanghae99.maannazan.domain.entity;

import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, message = "댓글을 입력해주세요.")
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
        if(commentRequestDto.getContent()==null||commentRequestDto.getContent().isEmpty()){
            throw new CustomException(CustomErrorCode.CONTENT_IS_EMPTY);
        }
        if(user.getId()==null || user.getId()<0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        if(post.getId()==null || post.getId()<0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        this.post = post;
        this.user = user;
        this.content = commentRequestDto.getContent();
        this.likecnt = 0;
    }

    public Comment(CommentRequestDto commentRequestDto, User user, Comment parentComment) {
        if(commentRequestDto.getContent()==null||commentRequestDto.getContent().isEmpty()){
            throw new CustomException(CustomErrorCode.CONTENT_IS_EMPTY);
        }
        if(user.getId()==null || user.getId()<0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        if(post.getId()==null || post.getId()<0){
            throw new CustomException(CustomErrorCode.FALSE_ID);
        }
        this.content = commentRequestDto.getContent();
        this.user = user;
        this.parent = parentComment;
        this.likecnt = 0;

    }

    public void update(CommentRequestDto commentRequestDto) {
        if(commentRequestDto.getContent()==null||commentRequestDto.getContent().isEmpty()){
            throw new CustomException(CustomErrorCode.CONTENT_IS_EMPTY);
        }
        this.content = commentRequestDto.getContent();
    }

    public void likeCount(int plusOrMinus) {  //좋아요 개수
        this.likecnt = plusOrMinus;
    }

    public List<Comment> getChildComments() {
        return this.children;
    }


}