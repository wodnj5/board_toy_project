package com.wodnj5.board.dto.request.user;

import lombok.Data;

@Data
public class UserSignupRequest {

    private String username;
    private String password;
    private String nickname;

}