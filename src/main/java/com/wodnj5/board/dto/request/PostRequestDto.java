package com.wodnj5.board.dto.request;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.PostFile;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.AwsS3Bucket;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostRequestDto {

    private String title;
    private String contents;
    private List<Long> deleteFileIds;
    private List<MultipartFile> uploadFiles;

    public Post toPostEntity(User user) {
        return Post.builder()
                .user(user)
                .title(title)
                .contents(contents)
                .uploadedAt(LocalDateTime.now())
                .build();
    }

    public List<PostFile> toPostFileEntities(Post post, AwsS3Bucket awsS3Bucket) {
        return uploadFiles.stream()
                .filter(file -> !Objects.requireNonNull(file.getOriginalFilename()).isEmpty())
                .map(file -> toPostFileEntity(file, post, awsS3Bucket))
                .toList();
    }

    private PostFile toPostFileEntity(MultipartFile file, Post post, AwsS3Bucket awsS3Bucket) {
        String key = "post" + File.separator + UUID.randomUUID();
        return PostFile.builder()
                .post(post)
                .name(file.getOriginalFilename())
                .s3Key(key)
                .url(awsS3Bucket.uploadObject(key, file))
                .uploadedAt(LocalDateTime.now())
                .build();
    }

}
