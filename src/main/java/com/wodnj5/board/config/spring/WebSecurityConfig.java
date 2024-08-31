package com.wodnj5.board.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        //.requestMatchers("/css/**","/js/**").permitAll()
                        .requestMatchers("/post").hasRole("USER")
                        .anyRequest().permitAll())
                .formLogin(formLoginConfigurer -> formLoginConfigurer
                        .loginPage("/login")
                        .permitAll())
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/")
                        .permitAll())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
