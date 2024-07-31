package com.wodnj5.board.service;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long signup(String email, String password, String nickname) {
        validateDuplicateEmail(email);
        validateDuplicateNickname(nickname);
        User user = new User(email, bCryptPasswordEncoder.encode(password), nickname, "ROLE_USER");
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateEmail(String email) {
        if(userRepository.findByEmail(email).isPresent()) throw new IllegalStateException("email is already used");
    }

    private void validateDuplicateNickname(String nickname) {
        if(userRepository.findByNickname(nickname).isPresent()) throw new IllegalStateException("email is already used");
    }
}
