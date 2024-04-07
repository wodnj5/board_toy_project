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

    public Long signUp(String username, String password, String email) {
        validateDuplicateUsername(username);
        User newbie = new User(username, password, email);
        userRepository.save(newbie);
        return newbie.getId();
    }

    private void validateDuplicateUsername(String username) {
        if(userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("USERNAME IS ALREADY USED");
        }
    }

    public User signIn(String username, String password) {
        Optional<User> find = userRepository.findByUsername(username);
        if(find.isEmpty()) {
            throw new IllegalStateException("USER IS NOT EXIST");
        }
        User user = find.get();
        validateCorrectPassword(user, password);
        return user;
    }

    private void validateCorrectPassword(User user, String password) {
        if(!user.getPassword().equals(password)) {
            throw new IllegalStateException("INCORRECT PASSWORD");
        }
    }
}
