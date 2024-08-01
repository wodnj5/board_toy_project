package com.wodnj5.board.service;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.PostFile;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.AwsS3Bucket;
import com.wodnj5.board.repository.PostRepository;
import java.io.File;
import java.util.List;
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
        if(!multipartFiles.isEmpty()) uploadFiles(post, multipartFiles);
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
            uploadFiles(post, multipartFiles);
        }
        return post.getId();
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        List<PostFile> postFiles = post.getPostFiles();
        if(!postFiles.isEmpty()) {
            postFiles.forEach(awsS3Bucket::deleteObject);
        }
        postRepository.delete(post);
    }

    private List<PostFile> uploadFiles(Post post, List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .map(file -> uploadFile(post, file))
                .toList();
    }

    private PostFile uploadFile(Post post, MultipartFile file) {
        StringBuilder sb = new StringBuilder();
        sb.append("post").append(File.separator).append(UUID.randomUUID());
        return new PostFile(post, sb.toString(), awsS3Bucket.uploadObject(sb.toString(), file));
    }
}
