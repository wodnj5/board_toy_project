package com.wodnj5.board.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity(String username, String password, String nickname, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void modify(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
