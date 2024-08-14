package com.wodnj5.board.dto.request;

import com.wodnj5.board.domain.User;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class UserRequestDto {

    private String email;
    private String password;
    private String nickname;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .build();
    }

}