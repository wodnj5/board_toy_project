package com.wodnj5.board.config.spring;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String message = null;
        if(exception instanceof InternalAuthenticationServiceException) {
            message = "User isn't exists.";
        }
        else if(exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            message = "Check email or password.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("/login").append("?").append("errorMessage").append("=").append(message);
        response.sendRedirect(sb.toString());
    }
}
