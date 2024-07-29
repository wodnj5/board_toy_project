package com.wodnj5.board.service;

import com.wodnj5.board.domain.User;
import com.wodnj5.board.domain.UserDetailsImpl;
import com.wodnj5.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
        return new UserDetailsImpl(user);
    }
}
