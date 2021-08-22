package com.example.demo.security.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
