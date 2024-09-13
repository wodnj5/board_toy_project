package com.wodnj5.board.dto.response.user;

import com.wodnj5.board.domain.UserEntity;
import lombok.Data;

@Data
public class LoginUserResponse {

    private Long id;
    private String nickname;

    public LoginUserResponse(UserEntity entity) {
        id = entity.getId();
        nickname = entity.getNickname();
    }
}
