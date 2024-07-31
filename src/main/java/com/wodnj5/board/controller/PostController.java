package com.wodnj5.board.controller;

import com.wodnj5.board.domain.UserDetailsImpl;
import com.wodnj5.board.dto.PostDto;
import com.wodnj5.board.service.PostService;
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
    public String home(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "home";
    }

    @GetMapping("/post/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/post/upload")
    public String upload(PostDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.upload(userDetails.getUser(), dto.getTitle(), dto.getContents(), dto.getPostFiles());
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String post(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findOne(id));
        return "post";
    }

    @PostMapping("/post/edit")
    public String edit(PostDto dto) {
        return "redirect:/post/" + postService.edit(dto.getId(), dto.getTitle(), dto.getContents(), dto.getPostFiles());
    }

    @GetMapping("/post/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/";
    }
}
