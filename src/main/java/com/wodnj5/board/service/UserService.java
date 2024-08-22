package com.wodnj5.board.service;

import com.wodnj5.board.domain.entity.UserEntity;
import com.wodnj5.board.dto.request.user.UserSignupRequest;
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

    public void signup(UserSignupRequest dto) {
        UserEntity userEntity = new UserEntity(dto.getUsername(),
                bCryptPasswordEncoder.encode(dto.getPassword()),
                dto.getNickname());
        userRepository.save(userEntity);
    }
}
