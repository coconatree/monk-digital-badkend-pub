package com.monk.service;

import com.monk.model.dto.ContactDTO;
import com.monk.model.request.NewContactRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface ContactServiceI {
    ContactDTO create(
            HttpServletRequest request,
            NewContactRequest newContactRequest
    ) throws MessagingException;
}
