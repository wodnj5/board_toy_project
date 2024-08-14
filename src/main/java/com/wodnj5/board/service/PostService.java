package com.wodnj5.board.service;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.PostFile;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.dto.request.PostRequestDto;
import com.wodnj5.board.repository.AwsS3Bucket;
import com.wodnj5.board.repository.PostFileRepository;
import com.wodnj5.board.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final AwsS3Bucket awsS3Bucket;

    public Long upload(User user, PostRequestDto dto) {
        Post post = dto.toPostEntity(user);
        List<PostFile> postFiles = dto.toPostFileEntities(post, awsS3Bucket);
        postRepository.save(post);
        postFileRepository.saveAll(postFiles);
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

    public Long edit(Long id, PostRequestDto dto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("post is not exist"));
        // 삭제해야하는 ID가 있다면 S3와 DB에서 파일 삭제
        List<Long> deleteFileIds = dto.getDeleteFileIds();
        if(Optional.ofNullable(deleteFileIds).isPresent()) {
            List<PostFile> deleteFiles = postFileRepository.findAllById(deleteFileIds);
            deleteFiles.forEach(awsS3Bucket::deleteObject);
            postFileRepository.deleteAll(deleteFiles);
        }
        // 내용 수정 후
        post.edit(dto.getTitle(), dto.getContents());
        // 새로운 파일 업로드
        List<PostFile> postFiles = dto.toPostFileEntities(post, awsS3Bucket);
        postFileRepository.saveAll(postFiles);
        return post.getId();
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("post is not exist"));
        List<PostFile> postFiles = post.getPostFiles();
        postFiles.forEach(awsS3Bucket::deleteObject);
        postFileRepository.deleteAll(postFiles);
        postRepository.delete(post);
    }
}
