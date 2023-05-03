package com.hanghae99.maannazan.domain.file;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {    //FIXME  S3버킷에 object를 업로드 하는 Service
    //FIXME Controller에서 전달받은 MultipartFile을 형식에 맞춰 S3 업로드 해주면 됨.


    @Value("${cloud.aws.s3.bucket}")
    //sanha--test
    private String bucket;
    private final AmazonS3 amazonS3;


    public String uploadFile(MultipartFile multipartFile) throws IOException {
        System.out.println(multipartFile);
        System.out.println(multipartFile);
        System.out.println(multipartFile);
        System.out.println(multipartFile);
        System.out.println(multipartFile);
        if (multipartFile == null) {
            return null;
        } else {
            String uuid= UUID.randomUUID().toString();
            String fileName =  uuid + "_" +multipartFile.getOriginalFilename();


            //파일 형식 구하기
            String ext = fileName.split("\\.")[1];
            String contentType = "";

            //content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
            switch (ext) {
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                case "png":
                    contentType = "image/png";
                    break;
                case "txt":
                    contentType = "text/plain";
                    break;
                case "csv":
                    contentType = "text/csv";
                    break;

            }

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(contentType);

                amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata));
                //.withCannedAcl(CannedAccessControlList.PublicRead);

            } catch (AmazonServiceException e) {
                return null;
//                e.printStackTrace();
            } catch (SdkClientException e) {
                return null;
//                e.printStackTrace();
            }


            //object 정보 가져오기
            ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);
            List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

            for (S3ObjectSummary object : objectSummaries) {
                System.out.println("object = " + object.toString());
            }
            return amazonS3.getUrl(bucket, fileName).toString();
        }


    }

}



