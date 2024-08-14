package com.wodnj5.board.controller;

import com.wodnj5.board.domain.CustomUserDetails;
import com.wodnj5.board.dto.request.PostRequestDto;
import com.wodnj5.board.dto.response.PostResponseDto;
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
        model.addAttribute("posts", postService.findAll()
                .stream()
                .map(PostResponseDto::fromEntity)
                .toList());
        return "home";
    }

    @GetMapping("/post")
    public String upload() {
        return "upload";
    }

    @PostMapping("/post")
    public String upload(@AuthenticationPrincipal CustomUserDetails userDetails, PostRequestDto dto) {
        postService.upload(userDetails.getUser(), dto);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String get(@PathVariable Long id, Model model) {
        model.addAttribute("post", PostResponseDto.fromEntity(postService.findOne(id)));
        return "post";
    }

    @PostMapping("/post/{id}/edit")
    public String edit(@PathVariable Long id, PostRequestDto dto) {
        return "redirect:/post/" + postService.edit(id, dto);
    }

    @PostMapping("/post/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/";
    }
}
