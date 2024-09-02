package com.wodnj5.board.exception;

public class UserNotFoundException extends IllegalStateException {

    @Override
    public String getMessage() {
        return "사용자가 존재하지 않습니다.";
    }
}
