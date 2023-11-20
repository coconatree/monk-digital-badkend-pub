package com.monk.model.request;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Getter
@ToString
@Component
public class NewMonkUserRequest {

    @Setter
    private String email;
    private String password;

    public NewMonkUserRequest() {}
    public NewMonkUserRequest(String email, String password) {
        setEmail(email);
        setPassword(password);
    }

    public void setPassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }
}
