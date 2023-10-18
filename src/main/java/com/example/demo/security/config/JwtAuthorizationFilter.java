package com.example.demo.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenManager jwtTokenManager;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, @NonNull JwtTokenManager jwtTokenManager) {
        super(authenticationManager);
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        jwtTokenManager.obtainUserFromJwt(request);
        chain.doFilter(request, response);
    }
}
