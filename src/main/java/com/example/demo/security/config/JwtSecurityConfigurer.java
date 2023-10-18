package com.example.demo.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class JwtSecurityConfigurer {

    private static final String LOGIN_PATH = "/api/v1/login";
    private static final String LOGOUT_PATH = "/api/v1/logout";
    private static final String CORS_ORIGIN_PROPERTY = "app.cors.origin";

    private final ObjectMapper objectMapper;
    private final Environment env;
    private final JwtTokenManager jwtTokenManager;

    protected String[] getWhiteListPatterns() {
        return new String[]{
            "/",
            LOGIN_PATH,
            LOGOUT_PATH,
            "/api/v1/user/register",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/actuator",
            "/actuator/**"
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout.logoutUrl(LOGOUT_PATH)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                    .permitAll())
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS).denyAll()
                    .requestMatchers(HttpMethod.TRACE).denyAll()
                    .requestMatchers(HttpMethod.HEAD).denyAll()
                    .requestMatchers(getWhiteListPatterns()).permitAll()
                    .requestMatchers("/api/v1/user/me").authenticated()
                    .anyRequest().authenticated())
                .addFilter(jwtAuthenticationFilter(authManager))
                .addFilter(jwtAuthorizationFilter(authManager))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    private CorsConfigurationSource corsConfiguration() {
        return r -> {
            var conf = new CorsConfiguration();
            conf.setAllowCredentials(true);
            conf.addAllowedHeader("*");
            conf.addAllowedMethod("*");
            var origin = env.getProperty(CORS_ORIGIN_PROPERTY);
            if (origin != null) {
                conf.addAllowedOriginPattern(origin);
            }
            return conf;
        };
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                authManager,
                jwtTokenManager,
                objectMapper);

        jwtAuthenticationFilter.setFilterProcessesUrl(LOGIN_PATH);
        return jwtAuthenticationFilter;
    }

    public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager) throws Exception {
        return new JwtAuthorizationFilter(authenticationManager, jwtTokenManager);
    }
}
