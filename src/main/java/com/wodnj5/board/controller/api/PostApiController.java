package com.wodnj5.board.controller.api;

import com.wodnj5.board.domain.entity.PostEntity;
import com.wodnj5.board.dto.request.post.PostSearchRequest;
import com.wodnj5.board.dto.response.post.PostResponse;
import com.wodnj5.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostRepository postRepository;

    @GetMapping("/api/post/search")
    public Page<PostResponse> search(PostSearchRequest dto, Pageable pageable) {
        Page<PostEntity> result = postRepository.search(dto.getSearch(), pageable);
        return result.map(PostResponse::new);
    }
}
