package com.wodnj5.board.dto.request.user;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String username;
    private String password;

}
