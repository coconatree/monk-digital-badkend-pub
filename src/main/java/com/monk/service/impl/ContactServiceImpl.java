package com.monk.service.impl;

import com.monk.mapper.ApplicationMapper;
import com.monk.model.dto.ContactDTO;
import com.monk.model.entity.Contact;
import com.monk.model.request.NewContactRequest;
import com.monk.repository.ContactRepository;
import com.monk.service.ContactServiceI;
import com.monk.service.EmailServiceI;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactServiceI {

    private final ContactRepository contactRepository;
    private final ApplicationMapper applicationMapper;
    private final EmailServiceI emailService;

    @Override
    @Transient
    public ContactDTO create(
            HttpServletRequest request,
            NewContactRequest  newContactRequest
    )
            throws MailException, MessagingException
    {

        Contact contact = applicationMapper.contactFromNewRequest(newContactRequest);
        contact.setCreatedAt(new Date(System.currentTimeMillis()));

        contactRepository.save(contact);
        emailService.sendContactEmail(contact.getEmail(), newContactRequest.getMessage());

        return applicationMapper.contactToDTO(contact);
    }
}
