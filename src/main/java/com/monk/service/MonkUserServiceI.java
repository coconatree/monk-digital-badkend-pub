package com.monk.service;

import com.monk.model.dto.MonkUserDTO;
import com.monk.model.entity.MonkUser;
import com.monk.model.request.NewEmailValidationRequest;
import com.monk.model.request.NewMonkUserRequest;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;

public interface MonkUserServiceI {
    MonkUserDTO createMonkUser(NewMonkUserRequest newMonkUserRequest);
    void sendEmailValidationMail(MonkUser monkUser) throws MessagingException;
    MonkUserDTO validateEmail(MonkUser monkUser, NewEmailValidationRequest newEmailValidationRequest);
    MonkUserDTO selectUser(Authentication authentication);
    boolean validateMonkUserEmail(String email);
    MonkUser createOrRetrieveMonkUserFromAuthToken(String token, String provider);
}
