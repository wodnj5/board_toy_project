package com.wodnj5.board.controller;

import com.wodnj5.board.domain.Post;
import com.wodnj5.board.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }
}
