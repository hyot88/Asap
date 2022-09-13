package com.fourtwod.config.auth.dto;

import com.fourtwod.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String email;
    private String registrationId;
    private String name;
    private String nickname;

    public SessionUser(User user) {
        this.email = user.getUserId().getEmail();
        this.registrationId = user.getUserId().getRegistrationId();
        this.name = user.getName();
        this.nickname = user.getNickname();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
