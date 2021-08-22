package com.example.demo.security.domain;

import com.example.demo.security.dtos.UserDto;
import com.example.demo.security.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ObtainMyProfileDomainService {

    private final UserService userService;
    private final AuditorAware<String> auditorAware;

    public UserDto obtainMyProfile() {
        return auditorAware.getCurrentAuditor().map(userService::findByUsername).get().get();
    }
}
