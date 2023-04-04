package com.hanghae99.maannazan.domain.post.dto;

import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;
import com.hanghae99.maannazan.domain.entity.Category;
import com.hanghae99.maannazan.domain.entity.Comment;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private String storename;

    private String title;

    private String description;

    private String image;

    private int likecnt;

    private boolean checks;

    private String nickname;   //Post 작성자

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;

    private boolean soju;
    private boolean beer;

    private double x;
    private double y;
    private List<CommentResponseDto> commentList = new ArrayList<>(); //만들어만 놨음

    public PostResponseDto(Category category) {   // 게시물 하나 조회
        this.storename = category.getPost().getStorename();
        this.title = category.getPost().getTitle();
        this.description = category.getPost().getDescription();
        this.image = category.getPost().getImage();
        this.likecnt = category.getPost().getLikecnt();
        this.checks = category.getPost().isChecks();
        this.nickname = category.getPost().getUser().getNickName();
        this.createAt = category.getPost().getCreatedAt();
        this.modifiedAt = category.getPost().getModifiedAt();
        this.soju = category.isSoju();
        this.beer = category.isBeer();
        this.x = category.getPost().getX();
        this.y = category.getPost().getY();

        List<Comment> comments = category.getPost().getCommentList();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : comments) {
                commentList.add(new CommentResponseDto(comment));
            }
            this.commentList = commentList;
        }
    }

    public PostResponseDto(Post post) {
        this.storename = post.getStorename();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.image = post.getImage();
        this.likecnt = post.getLikecnt();
        this.checks = post.isChecks();
        this.nickname = post.getUser().getNickName();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.x = post.getX();
        this.y = post.getY();
        List<Comment> comments = post.getCommentList();
        if (!comments.isEmpty()) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : comments) {
                commentList.add(new CommentResponseDto(comment));
            }
            this.commentList = commentList;
        }
    }
}
