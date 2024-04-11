package com.wodnj5.board.service;

import com.wodnj5.board.domain.Comment;
import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.CommentRepository;
import com.wodnj5.board.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long write(User user, Long postId, String content) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new RuntimeException("POST IS NOT EXCEPTION");
        }
        Comment comment = new Comment(user, post.get(), content);
        commentRepository.save(comment);
        return comment.getId();
    }
}
