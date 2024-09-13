package com.wodnj5.board.controller;

import com.wodnj5.board.domain.UserEntity;
import com.wodnj5.board.dto.request.user.UserLoginRequest;
import com.wodnj5.board.dto.request.user.UserModifyRequest;
import com.wodnj5.board.dto.request.user.UserSignupRequest;
import com.wodnj5.board.dto.response.user.LoginUserResponse;
import com.wodnj5.board.dto.response.user.UserInfoResponse;
import com.wodnj5.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

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
    public String login(HttpSession session, UserLoginRequest dto) {
        UserEntity loginUser = userService.login(dto);
        session.setAttribute("loginUser", new LoginUserResponse(loginUser));
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user")
    public String viewUserInfo(@SessionAttribute(name = "loginUser", required = false) LoginUserResponse loginUser, Model model) {
        if(loginUser == null) return "redirect:/login";
        UserEntity userInfo = userService.getLoginUserInfo(loginUser.getId());
        model.addAttribute("userInfo", new UserInfoResponse(userInfo));
        return "view_user";
    }

    @PostMapping("/user/modify")
    public String modify(@SessionAttribute(name = "loginUser", required = false) LoginUserResponse loginUser, UserModifyRequest dto) {
        if(loginUser == null) return "redirect:/login";
        userService.modify(loginUser.getId(), dto);
        return "redirect:/user";
    }
}
