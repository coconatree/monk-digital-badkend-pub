package com.monk.service.impl;

import com.monk.mapper.ApplicationMapper;
import com.monk.model.dto.MonkTokenDTO;
import com.monk.model.dto.MonkUserDTO;
import com.monk.model.entity.MonkUser;
import com.monk.model.pojo.EMonkRole;
import com.monk.model.request.NewEmailValidationRequest;
import com.monk.model.request.NewMonkUserRequest;
import com.monk.repository.MonkUserRepository;
import com.monk.service.EmailServiceI;
import com.monk.service.MonkTokenServiceI;
import com.monk.service.MonkUserServiceI;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@AllArgsConstructor
public class MonkUserServiceImpl implements MonkUserServiceI {

    private final MonkUserRepository monkUserRepository;
    private final ApplicationMapper applicationMapper;

    private final EmailServiceI emailService;
    private final MonkTokenServiceI monkTokenService;

    @Override
    @Transient
    public MonkUserDTO createMonkUser(NewMonkUserRequest newMonkUserRequest) {

        MonkUser monkUser = MonkUser.builder()
                .email(newMonkUserRequest.getEmail())
                .password(newMonkUserRequest.getPassword())
                .createdAt(new Date(System.currentTimeMillis()))
                .monkRole(EMonkRole.USER)
                .build();

        monkUserRepository.save(monkUser);
        return applicationMapper.monkUserToDTO(monkUser);
    }

    @Override
    public void sendEmailValidationMail(MonkUser monkUser) throws MessagingException {
        MonkTokenDTO token = monkTokenService.issueEmailValidationToken(monkUser);
        emailService.sendEmailValidationMail(
                monkUser,
                token.getToken(),
                token.getExpirationDate().getTime()
        );
    }

    @Override
    @Transient
    public MonkUserDTO validateEmail(
            MonkUser monkUser,
            NewEmailValidationRequest newEmailValidationRequest
    )
            throws JwtException
    {
        boolean isValid = monkTokenService.validateEmailToken(newEmailValidationRequest.getToken());
        if (!isValid) {
            throw new JwtException("Email validation token has expired");
        }
        monkUser.setEmailValidated(true);
        monkUser.setEmailValidatedAt(new Date(System.currentTimeMillis()));
        monkUserRepository.save(monkUser);
        return applicationMapper.monkUserToDTO(monkUser);
    }

    @Override
    public MonkUserDTO selectUser(Authentication authentication) {
        return mapMonkUserToDTO((MonkUser) authentication.getPrincipal());
    }

    @Override
    public boolean validateMonkUserEmail(String email) {
        return monkUserRepository.existsByEmail(email);
    }

    @Override
    public MonkUser createOrRetrieveMonkUserFromAuthToken(String token, String provider) {



        return null;
    }

    private MonkUserDTO mapMonkUserToDTO(MonkUser monkUser) {
        return MonkUserDTO.builder()
                .id(monkUser.getId())
                .email(monkUser.getEmail())
                .createdAt(monkUser.getCreatedAt())
                .role(monkUser.getMonkRole().name())
                .isEmailValidated(false)
                .isGoogleAccountConnected(false)
                .isFacebookAccountConnected(false)
                .isLinkedinAccountConnected(false)
                .build();
    }
}
