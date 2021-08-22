package com.example.demo.security.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        var username = jwtToUsername(request.getHeader(AUTH_HEADER_KEY));
        if (username != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    public void saveUserToJwt(HttpServletResponse response, String subject) {
        response.addHeader(AUTH_HEADER_KEY, userToJwt(subject));
    }

    public String jwtToUsername(String header) {
        if (Objects.nonNull(header) && header.startsWith(JwtTokenManager.TOKEN_PREFIX)) {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(header.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
        }
        return null;
    }

    public String userToJwt(String subject) {
        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(subject)
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(expirationDays).toInstant()))
                .signWith(secretKey)
                .compact();
    }
}
