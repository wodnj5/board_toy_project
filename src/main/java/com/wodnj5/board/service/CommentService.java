package com.wodnj5.board.service;

import com.wodnj5.board.domain.Comment;
import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.CommentRepository;
import com.wodnj5.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long write(User user, Long postId, String contents) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("게시글이 존재하지 않습니다."));
        Comment comment = new Comment(user, post, contents);
        commentRepository.save(comment);
        return comment.getId();
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
