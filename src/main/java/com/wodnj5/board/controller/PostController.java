package com.wodnj5.board.controller;

import com.wodnj5.board.domain.CustomUserDetails;
import com.wodnj5.board.domain.entity.PostEntity;
import com.wodnj5.board.dto.request.post.PostCreateRequest;
import com.wodnj5.board.dto.request.post.PostModifyRequest;
import com.wodnj5.board.dto.response.post.PostResponse;
import com.wodnj5.board.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String board(Model model) {
        List<PostEntity> posts = postService.findAll();
        model.addAttribute("posts", posts.stream()
                .map(PostResponse::new)
                .toList());
        return "board";
    }

    @PostMapping("/post")
    public String create(@AuthenticationPrincipal CustomUserDetails userDetails, PostCreateRequest dto) {
        postService.create(userDetails.getUserEntity(), dto);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String read(@PathVariable Long id, Model model) {
        model.addAttribute("post", new PostResponse(postService.findOne(id)));
        return "read";
    }

    @PostMapping("/post/{id}/modify")
    public String modify(@PathVariable Long id, PostModifyRequest dto) {
        return "redirect:/post/" + postService.edit(id, dto);
    }

    @PostMapping("/post/{id}/remove")
    public String remove(@PathVariable Long id) {
        postService.remove(id);
        return "redirect:/";
    }
}
