package com.wodnj5.board.controller;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.form.CommentForm;
import com.wodnj5.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/write/{id}")
    public String write(CommentForm form, @PathVariable Long id, @SessionAttribute(name = "user", required = false) User user) {
        if(user == null) {
            return "redirect:/user/login";
        }
        commentService.write(user, id, form.getContent());
        return "redirect:/post/read/" + id;
    }

    @GetMapping("/comment/delete/{postId}/{commentId}")
    public String delete(@PathVariable Long postId, @PathVariable Long commentId, @SessionAttribute(name = "user", required = false) User user ) {
        if(user == null) {
            return "redirect:/user/login";
        }
        commentService.delete(commentId);
        return "redirect:/post/read/" + postId;
    }
}
