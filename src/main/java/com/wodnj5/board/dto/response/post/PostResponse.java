package com.wodnj5.board.dto.response.post;

import com.wodnj5.board.domain.PostEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostResponse {

    private Long id;
    private Long userId;
    private String nickname;
    private String title;
    private String contents;
    private LocalDateTime createdDate;
    private Long views;
    private List<PostFileResponse> postFiles;

    public PostResponse(PostEntity entity) {
        id = entity.getId();
        userId = entity.getUser().getId();
        nickname = entity.getUser().getNickname();
        title = entity.getTitle();
        contents = entity.getContents();
        createdDate = entity.getCreatedDate();
        views = entity.getViews();
        postFiles = entity.getPostFiles().stream()
                .map(PostFileResponse::new)
                .toList();
    }

}
