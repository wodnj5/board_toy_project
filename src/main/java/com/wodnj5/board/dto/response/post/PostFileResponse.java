package com.wodnj5.board.dto.response.post;

import com.wodnj5.board.domain.entity.PostFileEntity;
import lombok.Data;

@Data
public class PostFileResponse {

    private Long id;
    private String originalFilename;
    private String path;

    public PostFileResponse(PostFileEntity entity) {
        id = entity.getId();
        originalFilename = entity.getPostFile().getOriginalFilename();
        path = entity.getPostFile().getPath();
    }
}
