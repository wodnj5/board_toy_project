package com.wodnj5.board.dto.request.user;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserModifyRequest {

    @NonNull
    private String password;
    @NonNull
    private String nickname;

}
