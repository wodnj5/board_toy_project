package com.wodnj5.board.controller;

import com.wodnj5.board.dto.UserDto;
import com.wodnj5.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserDto dto) {
        userService.signup(dto.getEmail(), dto.getPassword(), dto.getNickname());
        return "redirect:/";
    }
}
