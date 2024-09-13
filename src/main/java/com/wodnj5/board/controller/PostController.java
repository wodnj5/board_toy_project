package com.wodnj5.board.controller;

import com.wodnj5.board.domain.UserEntity;
import com.wodnj5.board.dto.request.post.PostCreateRequest;
import com.wodnj5.board.dto.request.post.PostModifyRequest;
import com.wodnj5.board.dto.request.post.PostSearchRequest;
import com.wodnj5.board.dto.response.post.PostResponse;
import com.wodnj5.board.dto.response.user.LoginUserResponse;
import com.wodnj5.board.service.PostService;
import com.wodnj5.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/")
    public String search(PostSearchRequest dto, @PageableDefault Pageable pageable, Model model) {
        Page<PostResponse> result = postService.search(dto, pageable).map(PostResponse::new);
        int start = result.getNumber() / 5 * 5;
        int end = Math.min(start + 5, result.getTotalPages());
        model.addAttribute("startPage", start);
        model.addAttribute("endPage", end);
        model.addAttribute("result", result);
        return "index";
    }

    @GetMapping("/post")
    public String post() {
        return "post";
    }

    @PostMapping("/post")
    public String post(@SessionAttribute(name = "loginUser", required = false) LoginUserResponse login, PostCreateRequest dto) {
        UserEntity user = userService.getLoginUserInfo(login.getId());
        postService.post(user, dto);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", new PostResponse(postService.view(id)));
        return "view_post";
    }

    @PostMapping("/post/{id}/modify")
    public String modify(@PathVariable Long id, PostModifyRequest dto) {
        postService.modify(id, dto);
        return "redirect:/post/" + id;
    }

    @PostMapping("/post/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/";
    }

}
