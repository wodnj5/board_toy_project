package com.wodnj5.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "post_files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String s3Key;
    @Column(nullable = false)
    private String url;
    private LocalDateTime uploadedAt;

    public PostFile(Post post, String name, String s3Key, String url) {
        this.post = post;
        this.name = name;
        this.s3Key = s3Key;
        this.url = url;
        this.uploadedAt = LocalDateTime.now();
    }

    public void delete() {
        post = null;
    }
}
