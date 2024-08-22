package com.wodnj5.board.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostFile {

    @Column
    private String originalFilename;
    @Column
    private String path;
    @Column
    private String fileKey;

    public PostFile(String originalFilename, String path, String fileKey) {
        this.originalFilename = originalFilename;
        this.path = path;
        this.fileKey = fileKey;
    }
}
