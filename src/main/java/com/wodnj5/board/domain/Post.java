package com.wodnj5.board.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostFile> postFiles = new ArrayList<>();
    private LocalDateTime uploadedAt;

    public Post(User user, String title, String contents) {
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.uploadedAt = LocalDateTime.now();
    }

    public void edit(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void saveFile(PostFile file) {
        postFiles.add(file);
    }

    public List<PostFile> findAllById(List<Long> ids) {
        return postFiles.stream().filter(file -> ids.contains(file.getId())).toList();
    }

    public void deleteFile(PostFile file) {
        file.delete();
        postFiles.remove(file);
    }

}
