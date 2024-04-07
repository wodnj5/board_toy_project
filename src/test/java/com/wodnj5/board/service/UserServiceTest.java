package com.wodnj5.board.service;

import static org.junit.jupiter.api.Assertions.fail;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    final String username = "Nam";
    final String password = "1234";
    final String email = "nam123@naver.com";

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void checkSignUp() {
        // 사용자 등록
        Long id = userService.signUp(username, password, email);
        Optional<User> user = userRepository.findById(id);
        // 사용자가 등록되지 않으면 실패
        if(user.isEmpty()) fail("USER IS NOT EXIST");
        // 찾은 사용자의 이름과 비밀번호가 일치하는 확인
        Assertions.assertEquals(username, user.get().getUsername());
        Assertions.assertEquals(password, user.get().getPassword());
    }

    @Test
    void validateWrongPassword() {
        // 사용자 등록
        userService.signUp(username, password, email);
        // 잘못된 비밀번호 입력 시 에러 발생
        Assertions.assertThrows(IllegalStateException.class, () -> userService.signIn(username, "5678"));
    }
}