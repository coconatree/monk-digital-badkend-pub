package com.monk.service;

import com.monk.model.entity.MonkUser;
import jakarta.mail.MessagingException;

public interface EmailServiceI {
    void sendAdminNotificationEmail(String message) throws MessagingException;
    void sendContactEmail(String to, String message) throws MessagingException;
    void sendEmailValidationMail(MonkUser monkUser, String token, long expiration) throws MessagingException;
}
