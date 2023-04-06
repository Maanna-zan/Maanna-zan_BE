package com.hanghae99.maannazan.domain.file;


import com.hanghae99.maannazan.domain.entity.Category;
import com.hanghae99.maannazan.domain.entity.File;
import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.entity.User;
import com.hanghae99.maannazan.domain.file.dto.FileDto;
import com.hanghae99.maannazan.domain.post.PostService;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import com.hanghae99.maannazan.domain.repository.CategoryRepository;
import com.hanghae99.maannazan.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public void save(FileDto fileDto) {
        File file = new File(fileDto.getTitle(), fileDto.getS3url());
        fileRepository.save(file);
    }

    public List<File> getFiles() {
        List<File> all = fileRepository.findAll();
        return all;
    }
}