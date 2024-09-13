package com.wodnj5.board.dto.response.user;

import com.wodnj5.board.domain.UserEntity;
import lombok.Data;

@Data
public class UserInfoResponse {

    private String username;
    private String nickname;

    public UserInfoResponse(UserEntity entity) {
        username = entity.getUsername();
        nickname = entity.getNickname();
    }
}
