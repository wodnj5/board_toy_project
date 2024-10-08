package com.wodnj5.board.service;

import com.wodnj5.board.domain.PostEntity;
import com.wodnj5.board.domain.PostFile;
import com.wodnj5.board.domain.PostFileEntity;
import com.wodnj5.board.domain.UserEntity;
import com.wodnj5.board.dto.request.post.PostCreateRequest;
import com.wodnj5.board.dto.request.post.PostModifyRequest;
import com.wodnj5.board.dto.request.post.PostSearchRequest;
import com.wodnj5.board.repository.AmazonS3Bucket;
import com.wodnj5.board.repository.post.PostRepository;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final AmazonS3Bucket amazonS3Bucket;

    @Transactional
    public void post(UserEntity userEntity, PostCreateRequest dto) {
        PostEntity post = new PostEntity(userEntity, dto.getTitle(), dto.getContents());
        uploadFiles(post, dto.getMultipartFiles());
        postRepository.save(post);
    }

    @Transactional
    public PostEntity view(Long id) {
        PostEntity post =  postRepository.findById(id).orElseThrow(IllegalStateException::new);
        post.increaseViews();
        return post;
    }

    private void uploadFiles(PostEntity post, List<MultipartFile> multipartFiles) {
        List<PostFileEntity> postFileEntities = multipartFiles.stream()
                .filter(file -> !Objects.requireNonNull(file).isEmpty())
                .map(file -> new PostFileEntity(post, createFile(file)))
                .toList();
        postFileEntities.forEach(post::addPostFile);
    }

    private PostFile createFile(MultipartFile multipartFile) {
        String fileKey = "post" + File.separator + UUID.randomUUID();
        return new PostFile(multipartFile.getOriginalFilename(), amazonS3Bucket.uploadObject(fileKey, multipartFile), fileKey);
    }

    @Transactional
    public void modify(Long id, PostModifyRequest dto) {
        PostEntity post = postRepository.findById(id).orElseThrow(IllegalStateException::new);
        // 삭제해야하는 ID가 있다면 S3와 DB에서 파일 삭제
        List<Long> uploadedFileIds = dto.getUploadedFileIds();
        if(Optional.ofNullable(uploadedFileIds).isPresent()) {
            List<PostFileEntity> deletedPostFiles = post.removeById(uploadedFileIds);
            deletedPostFiles.forEach(amazonS3Bucket::deleteObject);
        }
        post.modify(dto.getTitle(), dto.getContents());
        // 새로운 파일 업로드
        uploadFiles(post, dto.getMultipartFiles());
    }

    @Transactional
    public void delete(Long id) {
        PostEntity post = postRepository.findById(id).orElseThrow(IllegalStateException::new);
        List<PostFileEntity> postFileEntities = post.getPostFiles();
        postFileEntities.forEach(amazonS3Bucket::deleteObject);
        postRepository.delete(post);
    }

    public Page<PostEntity> search(PostSearchRequest dto, Pageable pageable) {
        return postRepository.search(dto.getSearch(), pageable);
    }
}
