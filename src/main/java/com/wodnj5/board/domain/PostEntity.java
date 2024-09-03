package com.wodnj5.board.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String title;
    @Column(columnDefinition = "text")
    private String contents;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostFileEntity> postFiles = new ArrayList<>();

    public PostEntity(UserEntity user, String title, String contents) {
        this.user = user;
        this.title = title;
        this.contents = contents;
    }

    public void modify(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public List<PostFileEntity> removeById(List<Long> uploadedPostFileIds) {
        List<PostFileEntity> uploadedPostFiles = postFiles.stream()
                .filter(file -> uploadedPostFileIds.contains(file.getId()))
                .toList();
        uploadedPostFiles.forEach(postFiles::remove);
        return uploadedPostFiles;
    }

    public void addPostFile(PostFileEntity postFileEntity) {
        postFiles.add(postFileEntity);
    }

}
