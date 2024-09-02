package com.wodnj5.board.service;

import com.wodnj5.board.domain.entity.UserEntity;
import com.wodnj5.board.dto.request.user.UserLoginRequest;
import com.wodnj5.board.dto.request.user.UserModifyRequest;
import com.wodnj5.board.dto.request.user.UserSignupRequest;
import com.wodnj5.board.exception.UserNotFoundException;
import com.wodnj5.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserEntity login(UserLoginRequest dto) {
        UserEntity user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if(!dto.getPassword().equals(user.getPassword())) {
            throw new IllegalStateException();
        }
        return user;
    }

    @Transactional
    public void signup(UserSignupRequest dto) {
        UserEntity userEntity = new UserEntity(dto.getUsername(),
                dto.getPassword(),
                dto.getNickname());
        userRepository.save(userEntity);
    }

    @Transactional
    public void modify(Long id, UserModifyRequest dto) {
        UserEntity user = userRepository.findById(id).orElseThrow(IllegalStateException::new);
        user.modify(dto.getPassword(), dto.getNickname());
    }
}
