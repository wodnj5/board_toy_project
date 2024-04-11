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
            throw new IllegalStateException("THIS EMAIL IS ALREADY USED");
        }
    }

    private void validateDuplicateUsername(String username) {
        if(userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("THIS USERNAME IS ALREADY USED");
        }
    }

    public User login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new IllegalStateException("USER IS NOT EXIST");
        }
        User loginUser = user.get();
        validateCorrectPassword(loginUser, password);
        return loginUser;
    }

    private void validateCorrectPassword(User user, String password) {
        if(!user.getPassword().equals(password)) {
            throw new IllegalStateException("INCORRECT PASSWORD");
        }
    }
}
