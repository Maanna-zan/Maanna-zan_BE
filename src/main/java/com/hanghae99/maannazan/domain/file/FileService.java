package com.hanghae99.maannazan.domain.file;


import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final PostRepository postRepository;

    public void save(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        postRepository.save(post);
    }

    public List<Post> getFiles() {
        List<Post> all = postRepository.findAll();
        return all;
    }
}