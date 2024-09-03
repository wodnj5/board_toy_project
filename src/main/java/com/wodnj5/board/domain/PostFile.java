package com.wodnj5.board.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostFile {

    private String originalFilename;
    private String path;
    private String fileKey;

    public PostFile(String originalFilename, String path, String fileKey) {
        this.originalFilename = originalFilename;
        this.path = path;
        this.fileKey = fileKey;
    }
}
