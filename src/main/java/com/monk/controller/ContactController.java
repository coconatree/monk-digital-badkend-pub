package com.monk.controller;

import com.monk.model.pojo.MonkApiResponse;
import com.monk.model.dto.ContactDTO;
import com.monk.model.request.NewContactRequest;
import com.monk.service.ContactServiceI;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/contact")
@CrossOrigin(origins = {"http://localhost:5173", "https://www.monk-digital.com"})
public class ContactController {

    private ContactServiceI contactService;

    @PostMapping(
            path = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<MonkApiResponse> createContact(
            HttpServletRequest request,
            @RequestBody NewContactRequest newContactRequest
    )
            throws MessagingException
    {
        ContactDTO contactDTO = contactService.create(request, newContactRequest);
        return ResponseEntity.ok(
                MonkApiResponse.builder()
                        .data(contactDTO)
                        .build()
        );
    }
}
