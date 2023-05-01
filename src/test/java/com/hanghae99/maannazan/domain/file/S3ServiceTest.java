package com.hanghae99.maannazan.domain.file;

import com.amazonaws.services.s3.AmazonS3;
import com.hanghae99.maannazan.domain.calendar.dto.CalendarResponseDto;
import com.hanghae99.maannazan.domain.user.UserService;
import com.hanghae99.maannazan.domain.user.dto.SignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;
    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private MultipartFile multipartFile;
    @Mock
    private String bucket;

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {

        @Test
        @DisplayName("S3버킷 이미지 업로드")
        void uploadFile() throws IOException {


            //when    //이것들을 실행할 때~
            String result = s3Service.uploadFile(multipartFile);
            String file = multipartFile.getOriginalFilename().split("\\.")[1];
            //then
            assertThat(result).isEqualTo( amazonS3.getUrl(bucket,file).toString());
        }
    }
}