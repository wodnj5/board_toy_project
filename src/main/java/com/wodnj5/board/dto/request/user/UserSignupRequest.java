package com.wodnj5.board.dto.request.user;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSignupRequest {

    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String nickname;

}