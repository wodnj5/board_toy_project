package com.wodnj5.board.dto.response;

import com.wodnj5.board.domain.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponseDto {

    private Long id;
    private Long userId;
    private String nickname;
    private String title;
    private String contents;
    private LocalDateTime uploadedAt;
    private List<PostFileResponseDto> postFiles;

    public static PostResponseDto fromEntity(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .contents(post.getContents())
                .uploadedAt(post.getUploadedAt())
                .postFiles(post.getPostFiles()
                        .stream()
                        .map(PostFileResponseDto::fromEntity)
                        .toList())
                .build();
    }
}
