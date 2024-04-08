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

    final String email = "jw123@naver.com";
    final String username = "jaewon";
    final String password = "1234";

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void join() {
        // 사용자 등록
        Long id = userService.join(email, password, username);
        Optional<User> user = userRepository.findById(id);
        // 사용자가 등록되지 않으면 실패
        if(user.isEmpty()) fail("USER IS NOT EXIST");
        // 찾은 사용자의 정보가 일치하는 확인
        Assertions.assertEquals(email, user.get().getEmail());
        Assertions.assertEquals(username, user.get().getUsername());
        Assertions.assertEquals(password, user.get().getPassword());
    }

    @Test
    void validateWrongPassword() {
        // 사용자 등록
        userService.join(username, password, email);
        // 잘못된 비밀번호 입력 시 에러 발생
        Assertions.assertThrows(IllegalStateException.class, () -> userService.login(username, "5678"));
    }
}