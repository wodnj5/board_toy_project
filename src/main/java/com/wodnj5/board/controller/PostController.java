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

    @GetMapping("/post/write")
    public String write() {
        return "write";
    }

    @PostMapping("/post/write")
    public String write(PostDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        postService.write(userDetails.getUser(), dto.getTitle(), dto.getContents());
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String read(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findOne(id));
        return "read";
    }

    @PostMapping("/post/{id}/edit")
    public String edit(@PathVariable Long id, PostDto dto, Model model) {
        postService.edit(id, dto.getTitle(), dto.getContents());
        return "redirect:/post/" + id;
    }

    @GetMapping("/post/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/";
    }
}
