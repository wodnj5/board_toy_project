package com.wodnj5.board.controller;

import com.wodnj5.board.domain.UserEntity;
import com.wodnj5.board.dto.request.user.UserLoginRequest;
import com.wodnj5.board.dto.request.user.UserModifyRequest;
import com.wodnj5.board.dto.request.user.UserSignupRequest;
import com.wodnj5.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(UserSignupRequest dto) {
        userService.signup(dto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, UserLoginRequest dto) {
        UserEntity user = userService.login(dto);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user")
    public String view() {
        return "view_user";
    }

    @PostMapping("/user/modify")
    public String modify(HttpServletRequest request, UserModifyRequest dto) {
        HttpSession session = request.getSession(false);
        Optional<UserEntity> user = Optional.ofNullable((UserEntity) session.getAttribute("user"));
        user.ifPresent(u -> userService.modify(u.getId(), dto));
        return "redirect:/user";
    }
}
