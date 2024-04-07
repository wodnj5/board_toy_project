package com.wodnj5.board.controller;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.form.SignInForm;
import com.wodnj5.board.form.SignUpForm;
import com.wodnj5.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signUp")
    public String signUp() {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(SignUpForm form) {
        userService.signUp(form.getUsername(), form.getPassword(), form.getEmail());
        return "redirect:/home";
    }

    @GetMapping("/signIn")
    public String signIn() {
        return "signIn";
    }

    @PostMapping("/signIn")
    public String signIn(SignInForm form, HttpServletRequest request) {
        User user = userService.signIn(form.getUsername(), form.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/signOut")
    public String signOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "redirect:/home";
    }
}
