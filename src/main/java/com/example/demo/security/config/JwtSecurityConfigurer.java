package com.example.demo.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
@RequiredArgsConstructor
public class JwtSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PATH = "/api/v1/login";
    private static final String LOGOUT_PATH = "/api/v1/logout";

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;
    private final ObjectMapper objectMapper;

    protected String[] getWhiteListPatterns() {
        return new String[]{
            "/",
            "/application",
            LOGIN_PATH,
            LOGOUT_PATH,
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
            "/webjars/**"};
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .logout()
                .logoutUrl(LOGOUT_PATH)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .permitAll().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).denyAll()
                .antMatchers(HttpMethod.TRACE).denyAll()
                .antMatchers(HttpMethod.HEAD).denyAll()
                .antMatchers(getWhiteListPatterns()).permitAll()
                .anyRequest().authenticated().and()
                .addFilter(jwtAuthenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokenManager))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), jwtTokenManager, objectMapper);
        jwtAuthenticationFilter.setFilterProcessesUrl(LOGIN_PATH);
        return jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
