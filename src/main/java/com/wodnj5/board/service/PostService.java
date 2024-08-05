package com.wodnj5.board.service;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.PostFile;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.AwsS3Bucket;
import com.wodnj5.board.repository.PostRepository;
import java.io.File;
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
        saveFiles(post, multipartFiles);
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

    public Long edit(Long id, String title, String contents, List<Long> fileIds, List<MultipartFile> multipartFiles) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        post.edit(title, contents);
        saveFiles(post, multipartFiles);
        if(Optional.ofNullable(fileIds).isPresent()) {
            List<PostFile> filesToDelete = post.findAllById(fileIds);
            filesToDelete.forEach(file -> {
                post.deleteFile(file);
                awsS3Bucket.deleteObject(file);
            });
        }
        return post.getId();
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        post.getPostFiles().forEach(awsS3Bucket::deleteObject);
        postRepository.delete(post);
    }

    private void saveFiles(Post post, List<MultipartFile> multipartFiles) {
        multipartFiles.stream()
                .filter(file -> Optional.ofNullable(file.getOriginalFilename()).isPresent())
                .forEach(file -> saveFile(post, file));
    }

    private void saveFile(Post post, MultipartFile file) {
        String s3Key = "post" + File.separator + UUID.randomUUID();
        post.saveFile(new PostFile(post, file.getOriginalFilename(), s3Key, awsS3Bucket.uploadObject(s3Key, file)));
    }
}
