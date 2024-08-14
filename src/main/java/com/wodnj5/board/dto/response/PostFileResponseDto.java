package com.wodnj5.board.dto.response;

import com.wodnj5.board.domain.PostFile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostFileResponseDto {

    private Long id;
    private String name;
    private String url;

    public static PostFileResponseDto fromEntity(PostFile postFile) {
        return PostFileResponseDto.builder()
                .id(postFile.getId())
                .name(postFile.getName())
                .url(postFile.getUrl())
                .build();
    }
}
