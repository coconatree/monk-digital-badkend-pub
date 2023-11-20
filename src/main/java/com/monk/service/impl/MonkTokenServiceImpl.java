package com.monk.service.impl;

import com.monk.model.entity.MonkUser;
import com.monk.model.dto.MonkTokenDTO;
import com.monk.service.MonkTokenServiceI;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.security.Key;
import io.jsonwebtoken.*;
import org.springframework.web.util.WebUtils;

import java.sql.Date;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class MonkTokenServiceImpl implements MonkTokenServiceI {
    private final Logger LOGGER = Logger.getLogger(MonkTokenServiceImpl.class.toString());
    private final String TOKEN_ISSUER = "www.monk-digital.com";

    @Value("${monk.jwt.secret}")
    private final String JWT_SECRET;

    @Value("${monk.jwt.email.secret}")
    private final String JWT_EMAIL_SECRET;

    @Value("${monk.jwt.account.expiration}")
    private final Long JWT_ACCOUNT_EXPIRATION_MS;

    @Value("${monk.jwt.refresh.expiration}")
    private final Long  JWT_REFRESH_EXPIRATION_MS;

    @Value("${monk.jwt.cookie.name}")
    private final String JWT_COOKIE_NAME;

    @Override
    public String getTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, JWT_COOKIE_NAME);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    @Override
    public ResponseCookie generateTokenCookie(MonkTokenDTO token) {
        return ResponseCookie.from(
                        JWT_COOKIE_NAME, token.getToken()
                )
                        .path("/")
                        .maxAge(24 * 60 * 60)
                        .httpOnly(true)
                        .sameSite("none")
                        .build();
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie
                .from(JWT_COOKIE_NAME, null)
                .path("/")
                .build();
    }

    @Override
    public MonkTokenDTO issueAuthToken(MonkUser monkUser) {
        return issueToken(monkUser, JWT_SECRET, JWT_ACCOUNT_EXPIRATION_MS);
    }

    @Override
    public MonkTokenDTO issueRefreshToken(MonkUser monkUser) {
        return issueToken(monkUser, JWT_SECRET, JWT_REFRESH_EXPIRATION_MS);
    }

    @Override
    public boolean validateOpenIdAuthToken(String token, String provider) {

        System.out.println(token + " - " + provider);

        return false;
    }

    @Override
    public boolean validateAuthToken(String token) {
        return validateToken(token, JWT_SECRET);
    }

    public boolean validateToken(String token, String secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key(secret))
                    .build()
                    .parse(token);
            return true;
        }
        catch (MalformedJwtException e) {
            LOGGER.info("Token is not valid");
        }
        catch (ExpiredJwtException e) {
            LOGGER.info("Token is expired");
        }
        catch (UnsupportedJwtException e) {
            LOGGER.info("Token is not unsupported");
        }
        catch (IllegalArgumentException e) {
            LOGGER.info("Token claims is empty");
        }
        return false;
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key(JWT_SECRET))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public MonkTokenDTO issueEmailValidationToken(MonkUser monkUser) {
        return issueToken(monkUser, JWT_EMAIL_SECRET, 3600000);
    }

    @Override
    public boolean validateEmailToken(String token) {
        return validateToken(token, JWT_EMAIL_SECRET);
    }

    @Override
    public boolean validateGoogleAuthToken(String openIdToken) {
        return false;
    }

    @Override
    public boolean validateLinkedinAuthToken(String openIdToken) {
        return false;
    }

    private MonkTokenDTO issueToken(MonkUser monkUser, String secret, long expiration) {

        Date currentDate = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(currentDate.getTime() + expiration);

        String token = Jwts.builder()
                .setIssuer(TOKEN_ISSUER)
                .setSubject(monkUser.getUsername())
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(key(secret), SignatureAlgorithm.HS256)
                .compact();

        return MonkTokenDTO.builder()
                .issuer(TOKEN_ISSUER)
                .expirationDate(expirationDate)
                .issuedAt(currentDate)
                .token(token)
                .build();
    }

    private Key key(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
