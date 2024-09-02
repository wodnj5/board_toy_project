package com.wodnj5.board.controller;

import com.wodnj5.board.domain.entity.UserEntity;
import com.wodnj5.board.dto.request.user.UserLoginRequest;
import com.wodnj5.board.dto.request.user.UserModifyRequest;
import com.wodnj5.board.dto.request.user.UserSignupRequest;
import com.wodnj5.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model, UserLoginRequest dto) {
        UserEntity user;
        try {
            user = userService.login(dto);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e);
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(UserSignupRequest dto) {
        try {
            userService.signup(dto);
        } catch (IllegalStateException e) {
            return "redirect:/signup";
        }
        return "redirect:/";
    }

    @GetMapping("/user")
    public String modify() {
        return "modify";
    }

    @PostMapping("/user/modify")
    public String modify(HttpServletRequest request, UserModifyRequest dto) {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        userService.modify(user.getId(), dto);
        return "redirect:/user";
    }
}
