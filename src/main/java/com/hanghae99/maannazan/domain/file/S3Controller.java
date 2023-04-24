package com.hanghae99.maannazan.domain.file;


import com.hanghae99.maannazan.domain.entity.Post;
import com.hanghae99.maannazan.domain.post.dto.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class S3Controller {

    private final S3Service s3Service;
    private final FileService fileService;


    @GetMapping("/api/upload")
    public String goToUpload() {
        return "upload";
    }

    @PostMapping("/api/upload")    //업로드 예시입니다 다른분들 참고하세요
    public String uploadFile(PostRequestDto postRequestDto) throws IOException {
        String url = s3Service.uploadFile(postRequestDto.getFile());
        postRequestDto.setS3Url(url);
        fileService.save(postRequestDto);
        return "redirect:/posts/1";
    }


    @GetMapping("/api/list")
    public String listPage(Model model) {
        List<Post> fileList =fileService.getFiles();
        model.addAttribute("fileList", fileList);
        return "list";
    }
}