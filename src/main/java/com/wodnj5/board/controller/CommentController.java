package com.wodnj5.board.controller;

import com.wodnj5.board.domain.UserDetailsImpl;
import com.wodnj5.board.dto.CommentDto;
import com.wodnj5.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{id}/comment/write")
    public String write(CommentDto dto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        commentService.write(userDetails.getUser(), id, dto.getContents());
        return "redirect:/post/" + id;
    }

    @GetMapping("/post/{postId}/comment/{commentId}/delete")
    public String delete(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.delete(commentId);
        return "redirect:/post/" + postId;
    }
}
