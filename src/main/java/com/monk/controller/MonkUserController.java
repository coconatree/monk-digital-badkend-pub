package com.monk.controller;

import com.monk.model.dto.MonkUserDTO;
import com.monk.model.entity.MonkUser;
import com.monk.model.pojo.MonkApiResponse;
import com.monk.model.request.NewEmailValidationRequest;
import com.monk.model.request.NewMonkUserRequest;
import com.monk.service.MonkUserServiceI;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/user")
@CrossOrigin(origins = {"http://localhost:5173", "https://www.monk-digital.com"})
public class MonkUserController {

    private MonkUserServiceI monkUserService;

    @GetMapping(
            path     = "/select",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    private ResponseEntity<MonkUserDTO> selectUser(
            Authentication authentication
    )
    {
        return ResponseEntity.ok(monkUserService.selectUser(authentication));
    }

    @PostMapping(
            path     = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    private ResponseEntity<MonkUserDTO> createMonkUser(
            @RequestBody NewMonkUserRequest newMonkUserRequest
    )
    {
        return ResponseEntity.ok(
                monkUserService.createMonkUser(newMonkUserRequest)
        );
    }

    @GetMapping(
            path     = "/validate/email",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    private ResponseEntity<MonkApiResponse> sendEmailValidation(
            Authentication authentication
    )
            throws MessagingException
    {
        MonkUser monkUser = (MonkUser) authentication.getPrincipal();

        monkUserService.sendEmailValidationMail(monkUser);


        return ResponseEntity.ok(MonkApiResponse.builder().build());
    }

    @PostMapping(
            path     = "/validate/email",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    private ResponseEntity<MonkUserDTO> validateEmail(
            Authentication authentication,
            @RequestBody NewEmailValidationRequest newEmailValidationRequest
    )
    {
        MonkUser monkUser = (MonkUser) authentication.getPrincipal();

        MonkUserDTO monkUserDTO = monkUserService
                .validateEmail(monkUser, newEmailValidationRequest);

        return ResponseEntity.ok(monkUserDTO);
    }
}
