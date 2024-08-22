package com.wodnj5.board.service;

import com.wodnj5.board.domain.entity.PostEntity;
import com.wodnj5.board.domain.entity.PostFile;
import com.wodnj5.board.domain.entity.PostFileEntity;
import com.wodnj5.board.domain.entity.UserEntity;
import com.wodnj5.board.dto.request.post.PostCreateRequest;
import com.wodnj5.board.dto.request.post.PostModifyRequest;
import com.wodnj5.board.repository.AmazonS3Bucket;
import com.wodnj5.board.repository.PostRepository;
import java.io.File;
import java.util.List;
import java.util.Objects;
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
    private final AmazonS3Bucket amazonS3Bucket;

    public void create(UserEntity userEntity, PostCreateRequest dto) {
        PostEntity post = new PostEntity(userEntity, dto.getTitle(), dto.getContents());
        List<PostFileEntity> postFileEntities = dto.getMultipartFiles().stream()
                .filter(file -> !Objects.requireNonNull(file).isEmpty())
                .map(file -> new PostFileEntity(post, createFile(file))
        ).toList();
        postFileEntities.forEach(post::addPostFile);
        postRepository.save(post);
    }

    private PostFile createFile(MultipartFile multipartFile) {
        String fileKey = "post" + File.separator + UUID.randomUUID();
        return new PostFile(multipartFile.getOriginalFilename(), amazonS3Bucket.uploadObject(fileKey, multipartFile), fileKey);
    }

    @Transactional(readOnly = true)
    public List<PostEntity> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PostEntity findOne(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalStateException("게시글이 존재하지 않습니다."));
    }

    public Long edit(Long id, PostModifyRequest dto) {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new IllegalStateException("게시글이 존재하지 않습니다."));
        // 삭제해야하는 ID가 있다면 S3와 DB에서 파일 삭제
        List<Long> uploadedFileIds = dto.getUploadedFileIds();
        if(Optional.ofNullable(uploadedFileIds).isPresent()) {
            List<PostFileEntity> deletedPostFiles = post.removeById(uploadedFileIds);
            deletedPostFiles.forEach(amazonS3Bucket::deleteObject);
        }
        post.edit(dto.getTitle(), dto.getContents());
        // 새로운 파일 업로드
        List<PostFileEntity> postFileEntities = dto.getMultipartFiles().stream()
                .map(file -> new PostFileEntity(post, createFile(file))
        ).toList();
        postFileEntities.forEach(post::addPostFile);
        return post.getId();
    }

    public void remove(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("게시글이 존재하지 않습니다."));
        List<PostFileEntity> postFileEntities = post.getPostFiles();
        postFileEntities.forEach(amazonS3Bucket::deleteObject);
        postRepository.delete(post);
    }
}
