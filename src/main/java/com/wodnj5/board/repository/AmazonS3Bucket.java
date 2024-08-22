package com.wodnj5.board.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wodnj5.board.domain.entity.PostFileEntity;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AmazonS3Bucket {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadObject(String fileKey, MultipartFile file) {
        // 중복 방지를 위한 새로운 이름 부여
        // 파일 형식 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileKey, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // 읽기 권한 주기
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return URLDecoder.decode(amazonS3.getUrl(bucket, fileKey).toString(), StandardCharsets.UTF_8);
    }

    public void deleteObject(PostFileEntity entity) {
        amazonS3.deleteObject(bucket, entity.getPostFile().getFileKey());
    }
}
