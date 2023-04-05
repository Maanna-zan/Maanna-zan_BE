package com.hanghae99.maannazan.domain.comment;


import com.hanghae99.maannazan.domain.comment.dto.CommentRequestDto;
import com.hanghae99.maannazan.domain.comment.dto.CommentResponseDto;
import com.hanghae99.maannazan.domain.entity.Comment;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.repository.CommentRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.ResponseMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //    1. 댓글 작성 메서드
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
//        게시글 존재 여부 확인. 없으면 예외처리
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );
        Comment comment = new Comment(post, user, commentRequestDto);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    //    2. 댓글 수정 메서드
    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto commentRequestDto, User user) {
//        게시글 존재 여부 확인. 없으면 예외처리
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );
//        댓글 존재 여부 확인. 없으면 예외처리
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글을 찾을 수 없습니다.")
        );
//        ADMIN이 아닌 멤버가 댓글의 해당 작성자가 아닐때 예외 처리 && !user.getRole().equals(UserRoleEnum.ADMIN
        if (!comment.getUser().getId().equals(user.getId()) ) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }
        comment.update(commentRequestDto);
        return new CommentResponseDto(comment);
    }

    //    3. 댓글 삭제 메서드
    @Transactional
    public ResponseEntity deleteComment(Long postId, Long commentId , User user) {
        //        게시글 존재 여부 확인. 없으면 예외처리
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );
//        댓글 존재 여부 확인. 없으면 예외처리
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글을 찾을 수 없습니다.")
        );
//        ADMIN이 아닌 멤버가 댓글의 해당 작성자가 아닐때 예외 처리 && !user.getRole().equals(UserRoleEnum.ADMIN)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
        return ResponseMessage.SuccessResponse("삭제 성공", "");
    }


}
