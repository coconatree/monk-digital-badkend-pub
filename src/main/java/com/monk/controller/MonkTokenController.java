package com.monk.controller;

import com.monk.model.entity.MonkUser;
import com.monk.model.dto.MonkTokenDTO;
import com.monk.model.request.NewOpenIdAuthRequest;
import com.monk.model.request.NewTokenRequest;
import com.monk.service.MonkTokenServiceI;
import com.monk.service.MonkUserServiceI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/token")
public class MonkTokenController {

    private final MonkTokenServiceI monkTokenService;
    private final MonkUserServiceI  userService;

    private final AuthenticationManager authenticationManager;

    @PostMapping(
            path     = "/issue/refresh",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<MonkTokenDTO> issueRefreshToken(
            @RequestBody NewTokenRequest newTokenRequest
    )
    {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                newTokenRequest.getEmail(),
                                newTokenRequest.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        MonkUser monkUser = (MonkUser) authentication.getPrincipal();

        MonkTokenDTO token = monkTokenService.issueRefreshToken(monkUser);

        ResponseCookie responseCookie = monkTokenService.generateTokenCookie(
                token
        );

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        responseCookie.toString()
                )
                .body(
                        token
                );
    }


    @GetMapping(
            path = "/issue/access",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<MonkTokenDTO> issueAccountToken(
            Authentication authentication
    )
    {
        MonkUser monkUser = (MonkUser) authentication.getPrincipal();
        MonkTokenDTO token = monkTokenService.issueAuthToken(monkUser);
        return ResponseEntity.ok(token);
    }

    @PostMapping(
            path     = "/issue/google/refresh",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<MonkTokenDTO> issueGoogleRefreshToken(
            @RequestBody NewOpenIdAuthRequest newOpenIdAuthRequest
    )
    {
        boolean valid = monkTokenService
                .validateOpenIdAuthToken(newOpenIdAuthRequest.getToken(), "google");

        if (!valid) {
            return ResponseEntity.badRequest().build();
        }

        MonkUser monkUser = userService.createOrRetrieveMonkUserFromAuthToken(
                newOpenIdAuthRequest.getToken(),
                "google"
        );

        MonkTokenDTO tokenDTO = monkTokenService.issueRefreshToken(monkUser);

        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping(
            path     = "/issue/linkedin/refresh",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<MonkTokenDTO> issueLinkedinRefreshToken(
            @RequestBody NewOpenIdAuthRequest newOpenIdAuthRequest
    )
    {
        boolean valid = monkTokenService
                .validateOpenIdAuthToken(newOpenIdAuthRequest.getToken(), "linkedin");

        if (!valid) {
            return ResponseEntity.badRequest().build();
        }

        MonkUser monkUser = userService.createOrRetrieveMonkUserFromAuthToken(
                newOpenIdAuthRequest.getToken(),
                "linkedin"
        );

        MonkTokenDTO tokenDTO = monkTokenService.issueRefreshToken(monkUser);

        return ResponseEntity.ok(tokenDTO);
    }

}
