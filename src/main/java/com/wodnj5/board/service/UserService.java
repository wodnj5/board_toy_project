package com.wodnj5.board.service;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public Long join(String email, String password, String username) {
        validateDuplicateEmail(email);
        validateDuplicateUsername(username);
        User newbie = new User(email, password, username);
        userRepository.save(newbie);
        return newbie.getId();
    }

    private void validateDuplicateEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 사용된 이메일입니다. 다른 이메일을 사용해주세요.");
        }
    }

    private void validateDuplicateUsername(String username) {
        if(userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("이미 사용된 닉네임입니다. 다른 닉네임을 사용해주세요.");
        }
    }

    public User login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 사용자입니다.");
        }
        User loginUser = user.get();
        validateCorrectPassword(loginUser, password);
        return loginUser;
    }

    private void validateCorrectPassword(User user, String password) {
        if(!user.getPassword().equals(password)) {
            throw new IllegalStateException("이메일 또는 비밀번호를 다시 확인해주세요.");
        }
    }
}
