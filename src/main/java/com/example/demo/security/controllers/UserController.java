package com.example.demo.security.controllers;

import com.example.demo.security.domain.ObtainMyProfileDomainService;
import com.example.demo.security.domain.UserRegiterDomainService;
import com.example.demo.security.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ObtainMyProfileDomainService obtainMyProfileDomainService;
    private final UserRegiterDomainService userRegiterDomainService;

    @PostMapping("/register")
    public void register(@RequestBody UserDto user) {
        userRegiterDomainService.register(user);
    }

    @GetMapping("/me")
    public UserDto obtainMyProfile() {
        return obtainMyProfileDomainService.obtainMyProfile();
    }
}
