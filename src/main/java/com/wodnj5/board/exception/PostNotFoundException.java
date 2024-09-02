package com.wodnj5.board.exception;

public class PostNotFoundException extends IllegalStateException {

    @Override
    public String getMessage() {
        return "게시글이 존재하지 않습니다.";
    }
}
