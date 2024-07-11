package com.wodnj5.board.controller;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.form.PostForm;
import com.wodnj5.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/write")
    public String write() {
        return "write";
    }

    @PostMapping("/post/write")
    public String write(PostForm form, @SessionAttribute(name = "user", required = false) User user, Model model) {
        if(user == null) {
            return "redirect:/user/login";
        }
        try {
            postService.write(user, form.getTitle(), form.getContent());
            return "redirect:/";
        } catch (IllegalStateException e) {
            model.addAttribute("em", e.getMessage());
            model.addAttribute("url", write());
            return "error";
        }
    }

    @GetMapping("/post/read/{id}")
    public String read(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("post", postService.findOne(id));
            return "read";
        } catch (IllegalStateException e) {
            model.addAttribute("em", e.getMessage());
            model.addAttribute("url", "redirect:/");
            return "error";
        }
    }

    @PostMapping("/post/edit/{id}")
    public String edit(@PathVariable Long id, PostForm form, @SessionAttribute(name = "user", required = false) User user, Model model) {
        if(user == null) {
            return "redirect:/user/login";
        }
        try {
            postService.edit(id, form.getTitle(), form.getContent());
            return "redirect:/post/read/" + id;
        } catch (IllegalStateException e) {
            model.addAttribute("em", e.getMessage());
            model.addAttribute("url", "redirect:/");
            return "error";
        }
    }

    @GetMapping("/post/delete/{id}")
    public String delete(@PathVariable Long id, @SessionAttribute(name = "user", required = false) User user, Model model) {
        if(user == null) {
            return "redirect:/user/login";
        }
        try {
            postService.delete(id);
            return "redirect:/";
        } catch (IllegalStateException e) {
            model.addAttribute("em", e.getMessage());
            model.addAttribute("url", "redirect:/");
            return "error";
        }
    }
}
