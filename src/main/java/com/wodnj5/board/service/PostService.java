package com.wodnj5.board.service;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.PostFile;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.AwsS3Bucket;
import com.wodnj5.board.repository.PostRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AwsS3Bucket awsS3Bucket;

    public Long upload(User user, String title, String content, List<MultipartFile> multipartFiles) {
        Post post = new Post(user, title, content);
        postRepository.save(post);
        uploadFiles(post, multipartFiles);
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

    public Long edit(Long id, String title, String contents, List<String> fileIds, List<MultipartFile> multipartFiles) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        post.edit(title, contents);
        List<PostFile> filesToDelete = post.deleteFiles(convertToLong(fileIds));
        deleteFiles(filesToDelete);
        uploadFiles(post, multipartFiles);
        return post.getId();
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        post.getPostFiles().forEach(awsS3Bucket::deleteObject);
        postRepository.delete(post);
    }

    private void uploadFiles(Post post, List<MultipartFile> multipartFiles) {
        multipartFiles.stream()
                .filter(file -> !file.getOriginalFilename().isEmpty())
                .forEach(file -> uploadToBucket(post, file));
    }

    private void deleteFiles(List<PostFile> filesToDelete) {
        filesToDelete.forEach(awsS3Bucket::deleteObject);
    }

    private void uploadToBucket(Post post, MultipartFile file) {
        String key = "post" + File.separator + UUID.randomUUID();
        post.saveFile(new PostFile(post, file.getOriginalFilename(), key, awsS3Bucket.uploadObject(key, file)));
    }

    private List<Long> convertToLong(List<String> fileIds) {
        if(Optional.ofNullable(fileIds).isEmpty()) return new ArrayList<>();
        return fileIds.stream()
                .map(Long::parseLong)
                .toList();
    }
}
