package com.wodnj5.board.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "post_files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFileEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;
    @Embedded
    private PostFile postFile;

    public PostFileEntity(PostEntity post, PostFile postFile) {
        this.post = post;
        this.postFile = postFile;
    }

}
