package com.wodnj5.board.controller;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.form.EditForm;
import com.wodnj5.board.form.WriteForm;
import com.wodnj5.board.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
    public String write(WriteForm form, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        postService.write(user, form.getTitle(), form.getContent());
        return "redirect:/";
    }

    @GetMapping("/post/read/{id}")
    public String read(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findOne(id));
        return "read";
    }

    @PostMapping("/post/edit")
    public String edit(EditForm form) {
        postService.edit(form.getId(), form.getTitle(), form.getContent());
        return "redirect:/post/read/" + form.getId();
    }
}
