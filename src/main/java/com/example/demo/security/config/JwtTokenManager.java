package com.example.demo.security.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager {

    public static final String AUTH_HEADER_KEY = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final SecretKey secretKey;
    private final long expirationDays;

    public JwtTokenManager(@Value("${jwt.token.secret}") String secret, @Value("${jwt.token.expiration-days:10}") long expirationDays) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationDays = expirationDays;
    }

    public void obtainUserFromJwt(HttpServletRequest request) {
        var jwtToken = extractJwtToken(request);
        if (jwtToken != null) {
            var username = extractUsernameFromJwt(jwtToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    public void saveUserToJwt(HttpServletResponse response, String subject) {
        response.addHeader(AUTH_HEADER_KEY, userToJwt(subject));
    }

    private String extractJwtToken(HttpServletRequest request) {
        var authHeader = request.getHeader(AUTH_HEADER_KEY);
        return (authHeader != null && authHeader.startsWith(JwtTokenManager.TOKEN_PREFIX)) ? 
                authHeader.replace(TOKEN_PREFIX, "") : null;
    }

    public String extractUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String userToJwt(String subject) {
        return TOKEN_PREFIX + Jwts.builder()
                .subject(subject)
                .expiration(Date.from(ZonedDateTime.now().plusDays(expirationDays).toInstant()))
                .signWith(secretKey)
                .compact();
    }
}
