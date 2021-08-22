package com.example.demo.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    private static final String[] AUTH_WHITELIST = {
        "/",
        "/application",
        "/login",
        "/user/register",
        "/*.html",
        "/**/*.html",
        "/**/*.css",
        "/**/*.js",
        "/**/*.jpg",
        "/**/*.gif",
        "/**/*.mp4",
        "/**/*.png",
        "/**/*.pdf",
        "/v3/api-docs/**",
        "/actuator",
        "/actuator/**",
        "/webjars/**",};

    protected String[] getWhiteListPatterns() {
        return AUTH_WHITELIST;
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).denyAll()
                .antMatchers(HttpMethod.TRACE).denyAll()
                .antMatchers(HttpMethod.HEAD).denyAll()
                .antMatchers(getWhiteListPatterns()).permitAll()
                .anyRequest().authenticated().and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokenManager))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
