package com.hanghae99.maannazan.domain.post;
import com.hanghae99.maannazan.domain.entity.Category;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.post.dto.PostResponseDto;
import com.hanghae99.maannazan.domain.repository.CategoryRepository;
import com.hanghae99.maannazan.domain.repository.CommentRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import com.hanghae99.maannazan.global.exception.CustomErrorCode;
import com.hanghae99.maannazan.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
//    private final CommentRepository commentRepository;

    public String createPost(PostRequestDto postrequestDto, User user) {
        Post post = new Post(postrequestDto, user);
        postRepository.saveAndFlush(post);
        if (postrequestDto.isBeer() || postrequestDto.isSoju()) {
            categoryRepository.saveAndFlush(new Category(postrequestDto, post));
        }
        return "게시물 작성 성공";
    }


    @Transactional(readOnly = true)
    public PostResponseDto getPostOne(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(CustomErrorCode.POST_NOT_FOUND)); //유지 보수땜에 있는게 나은지 db에 2번 접근하기 때문에 category만 찾는게 나을지
        Category category = categoryRepository.findByPostId(post.getId());
        if(category == null){
            return null;
        }
      /*  List<Comment> comments = post.getCommentList();
        if(!comments.isEmpty()){
        for (Comment comment : commentList) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }*/
        return new PostResponseDto(category);

   //     return new PostResponseDto(category);
    }



//    @Transactional(readOnly = true)     //개빡세다 전체 게시글 조회가 아니라 반경 x키로 안에 있는 게시글들을 조회해야해서..... 그리고 카테고리 별 검색 기능까지...
//    public List<PostResponseDto> getPosts(double x, double y, String category) {
//        List<Post> postList = postRepository.findByXAndY(x, y);    //findByXAndY가 아니라 x,y로 지정한 반경 몇키로 내의 게시글들을 조회해야함
//        List<Category> categoryList = categoryRepository.findAll();
//        List<PostResponseDto> PostResponseDtoList = new ArrayList<>();
//        for (Category category : categoryList) {
//            PostResponseDtoList.add(new PostResponseDto(category));
//
//        }
//        return PostResponseDtoList;
//    }

    @Transactional
    public String updatePost(Long postId, User user, PostRequestDto requestDto) {
        Post post = postRepository.findByUserIdAndId(user.getId(), postId);
        if(!(user.getId().equals(post.getUser().getId()))){
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        post.update(requestDto);
        return "업데이트 성공";
    }

    @Transactional
    public String deletePost(Long postId, User user) {
        Post post = postRepository.findByUserIdAndId(user.getId(), postId);
        if(!(user.getId().equals(post.getUser().getId()))){
            throw new CustomException(CustomErrorCode.NOT_AUTHOR);
        }
        Category category = categoryRepository.findByPostId(post.getId());
        categoryRepository.delete(category);
        postRepository.delete(post);
        return "게시글 삭제 완료";
    }
}
