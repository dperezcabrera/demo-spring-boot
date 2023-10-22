package com.example.demo;

import com.example.demo.security.domain.UserRegiterDomainService;
import com.example.demo.security.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final UserRegiterDomainService userRegiterDomainService;
    
    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        log.info("Initializer run ..");   
        userRegiterDomainService.register(new UserDto("user", "user", "user@email.com", "1"));
        log.info("Initializer ends");
    }
}
