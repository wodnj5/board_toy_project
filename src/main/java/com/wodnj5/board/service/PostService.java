package com.wodnj5.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.PostFile;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.PostRepository;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Long upload(User user, String title, String content, List<MultipartFile> multipartFiles) {
        Post post = new Post(user, title, content);
        postRepository.save(post);
        if(!multipartFiles.isEmpty()) saveFiles(post, multipartFiles);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Post findOne(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
    }

    public Long edit(Long id, String title, String contents, List<MultipartFile> multipartFiles) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        post.edit(title, contents);
        if(!multipartFiles.isEmpty()) {
            // 기존 파일 정보 덮어쓰기
            post.clearFiles();
            saveFiles(post, multipartFiles);
        }
        return post.getId();
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        List<PostFile> postFiles = post.getPostFiles();
        if(!postFiles.isEmpty()) {
            postFiles.forEach(this::deleteFromBucket);
        }
        postRepository.delete(post);
    }

    private void saveFiles(Post post, List<MultipartFile> multipartFiles) {
        multipartFiles.forEach(file -> createFiles(post, file));
    }

    private PostFile createFiles(Post post, MultipartFile file) {
        String fileName = UUID.randomUUID().toString();
        return new PostFile(post, fileName, uploadToBucket(fileName, file));
    }

    private String uploadToBucket(String fileName, MultipartFile file) {
        // 중복 방지를 위한 새로운 이름 부여
        // 파일 형식 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // 읽기 권한 주기
        } catch (IOException e) {
            e.printStackTrace();
        }
        return URLDecoder.decode(amazonS3.getUrl(bucket, fileName).toString(), StandardCharsets.UTF_8);
    }

    private void deleteFromBucket(PostFile file) {
        amazonS3.deleteObject(bucket, file.getFileName());
    }
}
