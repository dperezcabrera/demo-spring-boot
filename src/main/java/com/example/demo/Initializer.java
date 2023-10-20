package com.example.demo;

import com.example.demo.security.entities.User;
import com.example.demo.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        log.info("Initializer run ..");   
        userRepository.save(new User("user", "User", "user@email.com", "1"));
        log.info("Initializer ends");
    }
}
