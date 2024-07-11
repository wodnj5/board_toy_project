package com.wodnj5.board.service;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public Long write(User user, String title, String content) {
        Post post = new Post(user, title, content);
        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Post findOne(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isEmpty()) {
            throw new IllegalStateException("게시글이 존재하지 않습니다.");
        }
        return post.get();
    }

    public void edit(Long id, String title, String content) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isEmpty()) {
            throw new IllegalStateException("게시글이 존재하지 않습니다.");
        }
        post.get().edit(title, content);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
