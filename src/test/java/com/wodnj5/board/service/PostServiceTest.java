package com.wodnj5.board.service;

import static org.junit.jupiter.api.Assertions.fail;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.PostRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Test
    void write() {
        User user = new User("jw123@naver.com", "1234", "jaewon");
        Long id = postService.write(user, "title", "content");
        Optional<Post> find = postRepository.findById(id);
        if(find.isEmpty()) fail("POST IS NOT EXIST");
        Assertions.assertEquals(find.get().getUser(), user);
    }
}