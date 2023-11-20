package com.monk.service;

import com.monk.model.entity.MonkUser;
import com.monk.model.dto.MonkTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface MonkTokenServiceI {
    String getTokenFromCookies(HttpServletRequest request);
    ResponseCookie generateTokenCookie(MonkTokenDTO token);
    ResponseCookie getCleanJwtCookie();
    MonkTokenDTO issueAuthToken(MonkUser monkUser);
    MonkTokenDTO issueRefreshToken(MonkUser monkUser);
    boolean validateOpenIdAuthToken(String token, String provider);
    boolean validateAuthToken(String token);
    String extractUsername(String token);
    MonkTokenDTO issueEmailValidationToken(MonkUser monkUser);
    boolean validateEmailToken(String token);
    boolean validateGoogleAuthToken(String openIdToken);
    boolean validateLinkedinAuthToken(String openIdToken);
}
