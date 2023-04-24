package com.hanghae99.maannazan.domain.comment;


import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.domain.entity.Comment;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.CommentRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //    1. 댓글 작성 메서드
    @Transactional
    public void createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
//        게시글 존재 여부 확인. 없으면 예외처리
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(CustomErrorCode.POST_NOT_FOUND)
        );
        Comment comment = new Comment(post, user, commentRequestDto);
        commentRepository.save(comment);
    }

    //    2. 댓글 수정 메서드
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {

//        댓글 존재 여부 확인. 없으면 예외처리
        Comment comment = getCommentOne(commentId);
//        ADMIN이 아닌 멤버가 댓글의 해당 작성자가 아닐때 예외 처리 && !user.getRole().equals(UserRoleEnum.ADMIN
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        comment.update(commentRequestDto);

    }

    //    3. 댓글 삭제 메서드
    @Transactional
    public void deleteComment(Long commentId, User user) {
        //        게시글 존재 여부 확인. 없으면 예외처리

//        댓글 존재 여부 확인. 없으면 예외처리
        Comment comment = getCommentOne(commentId);
//        ADMIN이 아닌 멤버가 댓글의 해당 작성자가 아닐때 예외 처리 && !user.getRole().equals(UserRoleEnum.ADMIN)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        commentRepository.deleteById(commentId);
    }

    //대댓글 작성
    @Transactional
    public void createCommentList(CommentRequestDto commentRequestDto, User user, Long parentId) {

        Comment parentComment = null;
        if (parentId != null) {
            parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.COMMENT_NOT_FOUND));
        }
        Comment comment = new Comment(commentRequestDto, user, parentComment);
        commentRepository.saveAndFlush(comment);

        if (parentComment != null) {
            parentComment.getChildren().add(comment);
            commentRepository.saveAndFlush(parentComment);
        }


    }

    @Transactional
    public void deleteCommentList(Long commentId, User user) {
        Comment comment = getCommentOne(commentId);
        Long parentId = null;
        if (comment.getParent() != null) {
            parentId = comment.getParent().getId();
        }

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }

        // 대댓글이면 부모 댓글의 자식 댓글 목록에서 해당 대댓글을 삭제한다
        if (parentId != null) {
            Comment parentComment = comment.getParent();
            parentComment.getChildren().remove(comment);
            commentRepository.saveAndFlush(parentComment);
        }

        // 대댓글 또는 루트 댓글을 삭제한다
        commentRepository.delete(comment);
    }

    @Transactional
    public void updateCommentList(Long commentId, User user, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentOne(commentId);
        // 해당 댓글을 수정할 권한이 있는지 체크.
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }

        // 댓글 내용을 업데이트합니다.
        comment.update(commentRequestDto);

        // 자식 댓글 리스트를 업데이트합니다.
        List<Comment> childComments = comment.getChildComments();
        if (childComments != null) {
            for (Comment childComment : childComments) {
                updateComment(childComment.getId(), commentRequestDto, user);
            }
        }
    }



    //메서드
    public List<Comment> getCommentListByPost(Post post){    // 게시글에 달린 댓글리스트 조회
        return commentRepository.findByPost(post);
    }
    public List<Comment> getCommentListByUserId(Long id){    // 게시글에 달린 댓글리스트 조회
        return commentRepository.findByUserId(id);
    }

    public void deleteCommentAll(List<Comment> commentList){    // 게시글에 달린 댓글 전체 삭제
        commentRepository.deleteAll(commentList);
    }

    public Comment getCommentOne(Long commentId) {    // 단일 댓글 찾기(업데이트, 삭제)
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(CustomErrorCode.COMMENT_NOT_FOUND)
        );
    }
}