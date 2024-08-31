package com.wodnj5.board.controller;

import com.wodnj5.board.domain.CustomUserDetails;
import com.wodnj5.board.dto.request.user.UserModifyRequest;
import com.wodnj5.board.dto.request.user.UserSignupRequest;
import com.wodnj5.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(UserSignupRequest dto) {
        userService.signup(dto);
        return "redirect:/";
    }

    @GetMapping("/user")
    public String modify() {
        return "modify";
    }

    @PostMapping("/user/modify")
    public String modify(@AuthenticationPrincipal CustomUserDetails userDetails, UserModifyRequest dto) {
        userService.modify(userDetails.getId(), dto);
        return "redirect:/user";
    }
}
