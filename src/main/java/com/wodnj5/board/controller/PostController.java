package com.wodnj5.board.controller;

import com.wodnj5.board.domain.entity.UserEntity;
import com.wodnj5.board.dto.request.post.PostCreateRequest;
import com.wodnj5.board.dto.request.post.PostModifyRequest;
import com.wodnj5.board.dto.request.post.PostSearchRequest;
import com.wodnj5.board.dto.response.post.PostResponse;
import com.wodnj5.board.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public String home(Pageable pageable, Model model) {
        model.addAttribute("posts", postService.findAll(pageable).map(PostResponse::new));
        return "home";
    }

    @GetMapping("/post")
    public String create() {
        return "create";
    }

    @PostMapping("/post")
    public String create(HttpServletRequest request, PostCreateRequest dto) {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        postService.create(user, dto);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String read(@PathVariable Long id, Model model) {
        model.addAttribute("post", new PostResponse(postService.findOne(id)));
        return "read";
    }

    @PostMapping("/post/{id}/modify")
    public String modify(@PathVariable Long id, PostModifyRequest dto) {
        postService.modify(id, dto);
        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{id}/remove")
    public String remove(@PathVariable Long id) {
        postService.remove(id);
        return "redirect:/";
    }

    @GetMapping("/post/search")
    public String search(PostSearchRequest dto, Pageable pageable, Model model) {
        model.addAttribute("posts", postService.search(dto, pageable).map(PostResponse::new));
        return "home";
    }
}
